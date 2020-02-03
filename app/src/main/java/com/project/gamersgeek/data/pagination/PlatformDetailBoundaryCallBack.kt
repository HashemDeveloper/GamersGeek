package com.project.gamersgeek.data.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.platforms.PlatformDetails
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
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: PlatformDetails) {
        requestAndSaveData()
    }


    private fun requestAndSaveData() {
        if (this.isRequestInProgress) return

        this.isRequestInProgress = true
        launch {
            fetchAndSaveData(call = {
                rawgGameDbApi.getAllListOfVideoGamePlatform(lastRequestedPage, PAGE_SIZE, "id")
            }, onSuccess = {
                saveData(it.listOfResult)
            }, onError = {
                isRequestInProgress = false
                Timber.d(TAG, "Failed to fetch platform data: $it")
            })
        }

    }
    private fun saveData(dataList: List<PlatformDetails>) {
        launch {
            iPlatformDetailsDao.insert(dataList)
            lastRequestedPage++
            isRequestInProgress = false
        }
    }
    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
    companion object {
        private const val PAGE_SIZE = 10
        @JvmStatic private val TAG: String = PlatformDetailBoundaryCallBack::class.java.canonicalName!!
    }
}