package com.project.gamersgeek.data

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.pagination.PagingDataListDispatcher
import com.project.gamersgeek.data.pagination.PlatformDetailBoundaryCallBack
import com.project.gamersgeek.data.pagination.PlatformDetailsDataSourceFactory
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.paging.NetworkState
import java.util.concurrent.Executor
import javax.inject.Inject

class GamerGeekRepository @Inject constructor(): IGamerGeekRepository {
    @Inject
    lateinit var iRawgGameDbApi: IRawgGameDbApi
    @Inject
    lateinit var iPlatformDetailsDao: IPlatformDetailsDao

    override fun getPlatformDetailsPagedData(pageSize: Int): PagingDataListDispatcher<PlatformDetails> {
       return getPlatformDetailsFromLocalDb(pageSize)
    }

    private fun getPlatformDetailsFromLocalDb(pageSize: Int): PagingDataListDispatcher<PlatformDetails> {
        val dataSourceFactory: DataSource.Factory<Int, PlatformDetails> = this.iPlatformDetailsDao.getAllPlatformDetails()
        val platformDetailBoundaryCallBack = PlatformDetailBoundaryCallBack(this.iPlatformDetailsDao, this.iRawgGameDbApi)
        val triggerRefresh = MutableLiveData<Unit>()
        val refreshState: LiveData<NetworkState> = triggerRefresh.switchMap {
            refresh(platformDetailBoundaryCallBack)
        }
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        val livePagedList = LivePagedListBuilder(dataSourceFactory, pageListConfig(pageSize))
            .setBoundaryCallback(platformDetailBoundaryCallBack)
            .setFetchExecutor(fetchExecutor)
            .build()
        return PagingDataListDispatcher(
            pagedList = livePagedList,
            networkState = platformDetailBoundaryCallBack.netWorkState,
            retry = {
                platformDetailBoundaryCallBack.helper.isAllRetryFailed()
            },
            refresh = {
                triggerRefresh.value = null
            },
            refreshState = refreshState
        )
    }

    private fun refresh(platformDetailBoundaryCallBack: PlatformDetailBoundaryCallBack): LiveData<NetworkState> {
        return platformDetailBoundaryCallBack.refresh()
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val INITIAL_LOAD_SIZE_HINT = PAGE_SIZE * 3
        private const val MAX_SIZE: Int = PagedList.Config.MAX_SIZE_UNBOUNDED
        fun pageListConfig(pageSize: Int) = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setPrefetchDistance(pageSize)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setMaxSize(MAX_SIZE)
            .build()
    }
}