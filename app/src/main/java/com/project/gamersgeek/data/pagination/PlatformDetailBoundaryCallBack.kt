package com.project.gamersgeek.data.pagination

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
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
    private var lastRequestedPage = 1
    private val job = Job()
    val helper = PagingRequestHelper()
    val netWorkState = helper.createNetworkStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        this.helper.runIfNotRunning(RequestType.INITIAL) {
            requestAndSaveData()
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: PlatformDetails) {
        this.helper.runIfNotRunning(RequestType.INITIAL) {
            requestAndSaveData()
        }
    }

    private fun requestAndSaveData(): Request {
        return object : Request {
            override fun run(requestCallback: Request.Callback) {
                launch {
                    fetchAndSaveData(call = {
                        rawgGameDbApi.getAllListOfVideoGamePlatform(lastRequestedPage, PAGE_SIZE, "id")
                    }, onSuccess = {
                        saveData(it.listOfResult)
                        requestCallback.recordSuccess()
                    }, onError = {
                        requestCallback.recordFailure()
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
                rawgGameDbApi.getAllListOfVideoGamePlatform(lastRequestedPage, PAGE_SIZE, "id")
            }, onSuccess = {
                saveData(it.listOfResult)
                networkState.postValue(NetworkState.LOADED)
            }, onError = {
                networkState.postValue(NetworkState.error(it))
                Timber.d(TAG, "Failed to fetch platform data: $it")
            })
        }
        return networkState
    }
    private fun saveData(dataList: List<PlatformDetails>) {
        launch {
            iPlatformDetailsDao.insert(dataList)
            lastRequestedPage++
        }
    }


    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
    companion object {
        private const val PAGE_SIZE = 10
        @JvmStatic private val TAG: String = PlatformDetailBoundaryCallBack::class.java.canonicalName!!
    }
}