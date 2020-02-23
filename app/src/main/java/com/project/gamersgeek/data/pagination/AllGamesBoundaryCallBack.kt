package com.project.gamersgeek.data.pagination

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
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

    override fun onZeroItemsLoaded() {
        this.paginHelper.runIfNotRunning(RequestType.INITIAL) {
            requestAndSaveData()
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Results) {
        this.paginHelper.runIfNotRunning(RequestType.AFTER) {
            requestAndSaveData()
        }
    }


    private fun requestAndSaveData(): Request {
        val network = MutableLiveData<NetworkState>()
        return object : Request {
            override fun run(requestCallback: Request.Callback) {
                launch {
                    fetchAndSaveData(call = {
                        gamerIRawgGameDbApi.fetchAllGames(1, PAGE_SIZE)
                    }, onSuccess = {
                        saveData(it.results, requestCallback)
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

    private fun saveData(
        it: @RawValue List<Results>?,
        requestCallback: Request.Callback
    ) {
        launch {
            it?.let {results ->
                this@AllGamesBoundaryCallBack.gameResultDao.insertGameList(results)
                requestCallback.recordSuccess()
            }
        }
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
        private const val PAGE_SIZE = 30
        @JvmStatic private val TAG: String = AllGamesBoundaryCallBack::class.java.canonicalName!!
    }
}