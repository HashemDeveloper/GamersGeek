package com.project.gamersgeek.data.pagination

import android.net.Uri
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.paging.*
import kotlinx.android.parcel.RawValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AllGamesBoundaryCallBack @Inject constructor(private val gameResultDao: IGameResultDao,
                                                   private val gamerIRawgGameDbApi: IRawgGameDbApi): PagedList.BoundaryCallback<Results>(), CoroutineScope{

    private val job = Job()
    val paginHelper = PagingRequestHelper()
    val networkState = paginHelper.createNetworkStatusLiveData()
    private var pageCount: Int = 1
    private val cache: MutableMap<Int, Results> = HashMap()

    override fun onZeroItemsLoaded() {
        this.paginHelper.runIfNotRunning(RequestType.INITIAL) {
            requestAndSaveData()
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Results) {
        this.paginHelper.runIfNotRunning(RequestType.AFTER) {
            requestAndSaveData()
        }
        super.onItemAtEndLoaded(itemAtEnd)
    }

    private fun requestAndSaveData(): Request {
        val network = MutableLiveData<NetworkState>()
        return object : Request {
            override fun run(requestCallback: Request.Callback) {
                launch {
                    fetchAndSaveData(call = {
                        gamerIRawgGameDbApi.fetchAllGames(pageCount, PAGE_SIZE)
                    }, onSuccess = {
                        saveData(it.results, requestCallback)
                        setupPageCount(it)
                    }, onError = {
                        requestCallback.recordFailure(it)
                        if (BuildConfig.DEBUG) {
                            Timber.d("Error fetching data: $it")
                        }
                    })
                }
            }
        }
    }
    private fun setupPageCount(it: GameListRes) {
        var nextUrl: String? = ""
        it.next?.let {url ->
            nextUrl = url
        }
        if (nextUrl?.isNotEmpty()!!) {
            val nextUri: Uri = Uri.parse(nextUrl)
            if (nextUri.getQueryParameter("page") != null) {
                val query: String? = nextUri.getQueryParameter("page")!!
                if (query != null && query.isNotEmpty()) {
                    pageCount = nextUri.getQueryParameter("page")?.toInt()!!
                }
            }
        }
    }
    private fun saveData(
        resultList: @RawValue List<Results>?,
        requestCallback: Request.Callback
    ) {
        var list: MutableList<Results>?= null
        resultList?.let {
            list = getCachedData(it)
        }
        launch {
            list?.let { results ->
                this@AllGamesBoundaryCallBack.gameResultDao.insertGameList(results)
                requestCallback.recordSuccess()
                results.clear()
            }
        }
    }
    private fun getCachedData(dataList: List<Results>): MutableList<Results>? {
        for (data: Results in dataList) {
            if (!this.cache.containsKey(data.id)) {
                this.cache[data.id] = data
            }
        }
        return ArrayList(this.cache.values)
    }
    @MainThread
    fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        launch {
            fetchAndSaveData(call = {
                gamerIRawgGameDbApi.fetchAllGames(1, PAGE_SIZE)
            }, onSuccess = {
                deleteAndSaveData(it.results)
                networkState.value = NetworkState.LOADED
            }, onError = {
                networkState.value = NetworkState.error(it)
                if (BuildConfig.DEBUG) {
                    Timber.d("Failed to refresh data: $it")
                }
            })
        }
        return networkState
    }

    private fun deleteAndSaveData(results: @RawValue List<Results>?) {
        launch {
            results?.let {
                this@AllGamesBoundaryCallBack.gameResultDao.clearAllGameResults()
                this@AllGamesBoundaryCallBack.gameResultDao.insertGameList(it)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    companion object {
        private const val PAGE_SIZE = 50
        @JvmStatic private val TAG: String = AllGamesBoundaryCallBack::class.java.canonicalName!!
    }
}