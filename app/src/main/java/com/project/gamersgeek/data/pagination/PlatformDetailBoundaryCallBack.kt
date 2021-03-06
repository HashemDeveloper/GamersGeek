package com.project.gamersgeek.data.pagination

import android.net.Uri
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformDetailBoundaryCallBack @Inject constructor(private val iPlatformDetailsDao: IPlatformDetailsDao,
                                                         private val rawgGameDbApi: IRawgGameDbApi): PagedList.BoundaryCallback<PlatformDetails>(), CoroutineScope {
    private val job = Job()
    val helper = PagingRequestHelper()
    val netWorkState = helper.createNetworkStatusLiveData()
    private var pageCount: Int = 1
    private val cache: MutableMap<Int, PlatformDetails> = HashMap()
    @MainThread
    override fun onZeroItemsLoaded() {
        this.helper.runIfNotRunning(RequestType.INITIAL) {
            requestAndSaveData()
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: PlatformDetails) {
        this.helper.runIfNotRunning(RequestType.AFTER) {
            requestAndSaveData()
        }
    }

    private fun requestAndSaveData(): Request {
        return object : Request {
            override fun run(requestCallback: Request.Callback) {
                launch {
                    fetchAndSaveData(call = {
                        rawgGameDbApi.getAllListOfVideoGamePlatform(pageCount, PAGE_SIZE, "id")
                    }, onSuccess = {
                        it.results?.let { r ->
                            saveData(r, requestCallback)
                        }
                        setupPageCount(it)
                    }, onError = {
                        requestCallback.recordFailure(it)
                        Timber.d(TAG, "Failed to fetch platform data: $it")
                    })
                }
            }
        }
    }
    @MainThread
    fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        launch {
            fetchAndSaveData(call = {
                rawgGameDbApi.getAllListOfVideoGamePlatform(pageCount, PAGE_SIZE, "id")
            }, onSuccess = {
                networkState.postValue(NetworkState.LOADED)
                setupPageCount(it)
                it.results?.let { r ->
                    updateData(r)
                }
            }, onError = {
                networkState.postValue(NetworkState.error(it))
                Timber.d(TAG, "Failed to fetch platform data: $it")
            })
        }
        return networkState
    }

    private fun setupPageCount(it: BaseResModel<PlatformDetails>) {
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
        dataList: List<PlatformDetails>,
        requestCallback: Request.Callback
    ) {
        getCachedItems(dataList)?.let {

        }
        val list: MutableList<PlatformDetails>? = getCachedItems(dataList)
        launch {
            list?.let {
                iPlatformDetailsDao.insert(it)
                it.clear()
            }
            requestCallback.recordSuccess()
        }
    }
    private fun updateData(dataList: List<PlatformDetails>) {
        val list: MutableList<PlatformDetails>? = getCachedItems(dataList)
        launch {
            list?.let {
                iPlatformDetailsDao.update(it)
            }
        }
    }

    private fun getCachedItems(dataList: List<PlatformDetails>): MutableList<PlatformDetails>? {
        for (data: PlatformDetails in dataList) {
            if (!cache.containsKey(data.id)) {
                cache[data.id] = data
            }
        }
        return ArrayList(cache.values)
    }


    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
    companion object {
        private const val PAGE_SIZE = 10
        @JvmStatic private val TAG: String = PlatformDetailBoundaryCallBack::class.java.canonicalName!!
    }
}