package com.project.gamersgeek.data.pagination

import androidx.paging.PageKeyedDataSource
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.fetchAndSaveData
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformDetailsDataSource(
    private val iPlatformDetailsDao: IPlatformDetailsDao,
    private val rawgGameDbApi: IRawgGameDbApi
) : PageKeyedDataSource<Int, PlatformDetails>(), CoroutineScope {
    private val job = Job()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PlatformDetails>
    ) {
        requestAndSaveData(1, params.requestedLoadSize)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PlatformDetails>) {
        val page: Int = params.key
        requestAndSaveData(page, params.requestedLoadSize)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PlatformDetails>) {
        val page: Int = params.key
        requestAndSaveData(page, params.requestedLoadSize)
    }

    private fun requestAndSaveData(page: Int, pageSize: Int) {
        launch {
            fetchAndSaveData(call = {
                rawgGameDbApi.getAllListOfVideoGamePlatform(page, pageSize, "id")
            }, onSuccess = {
                saveData(it.listOfResult)
            }, onError = {
                if (BuildConfig.DEBUG) {
                    Timber.d(TAG, "Failed to fetch platform data: $it")
                }
            })
        }

    }

    private fun saveData(dataList: List<PlatformDetails>) {
        launch {
            iPlatformDetailsDao.insert(dataList)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    companion object {
        @JvmStatic
        private val TAG: String = PlatformDetailsDataSource::class.java.canonicalName!!
    }
}