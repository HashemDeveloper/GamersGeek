package com.project.gamersgeek.data

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.pagination.AllGamesBoundaryCallBack
import com.project.gamersgeek.data.pagination.PagingDataListDispatcher
import com.project.gamersgeek.data.pagination.PlatformDetailBoundaryCallBack
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.paging.NetworkState
import java.lang.IllegalArgumentException
import java.util.concurrent.Executor
import javax.inject.Inject

class GamerGeekRepository @Inject constructor(): IGamerGeekRepository {
    @Inject
    lateinit var iRawgGameDbApi: IRawgGameDbApi
    @Inject
    lateinit var iPlatformDetailsDao: IPlatformDetailsDao
    @Inject
    lateinit var iGameResultDao: IGameResultDao

    override fun getPlatformDetailsPagedData(pageSize: Int): PagingDataListDispatcher<PlatformDetails> {
       return getPlatformDetailsFromLocalDb(pageSize)
    }

    private fun getPlatformDetailsFromLocalDb(pageSize: Int): PagingDataListDispatcher<PlatformDetails> {
        val dataSourceFactory: DataSource.Factory<Int, PlatformDetails> = this.iPlatformDetailsDao.getAllPlatformDetails()
        val platformDetailBoundaryCallBack = PlatformDetailBoundaryCallBack(this.iPlatformDetailsDao, this.iRawgGameDbApi)
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        val livePagedList: LiveData<PagedList<PlatformDetails>> = LivePagedListBuilder(dataSourceFactory, pageListConfig(pageSize))
            .setBoundaryCallback(platformDetailBoundaryCallBack)
            .setFetchExecutor(fetchExecutor)
            .build()
        return setPagedListData(platformDetailBoundaryCallBack, livePagedList)
    }

    private fun <T> refresh(boundaryCallback: T): LiveData<NetworkState> {
       return when (boundaryCallback) {
           is PlatformDetailBoundaryCallBack -> boundaryCallback.refresh()
           is AllGamesBoundaryCallBack -> boundaryCallback.refresh()
           else -> throw IllegalArgumentException("Not valid Boundary CallBack")
       }
    }

    override fun getAllGamesPagedData(pageSize: Int): PagingDataListDispatcher<Results> {
        return processAllGameListData(pageSize)
    }

    private fun processAllGameListData(pageSize: Int): PagingDataListDispatcher<Results> {
        val allGameDataSource: DataSource.Factory<Int, Results> = this.iGameResultDao.getAllGameResultForDatasource()
        val allGamesBoundaryCallBack = AllGamesBoundaryCallBack(this.iGameResultDao, this.iRawgGameDbApi)
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        val livePagedList: LiveData<PagedList<Results>> = LivePagedListBuilder(allGameDataSource, pageListConfig(pageSize))
            .setBoundaryCallback(allGamesBoundaryCallBack)
            .setFetchExecutor(fetchExecutor)
            .build()
        return setPagedListData(allGamesBoundaryCallBack, livePagedList)
    }

    private fun <T, S> setPagedListData(boundaryCallBack: T, livePagedList: LiveData<PagedList<S>>): PagingDataListDispatcher<S> {
        return when (boundaryCallBack) {
            is PlatformDetailBoundaryCallBack -> {
                setupPlatformDetailsPagingDispatcher(boundaryCallBack, livePagedList)
            }
            is AllGamesBoundaryCallBack -> {
                setupAllGamesPagingDispatcher(boundaryCallBack, livePagedList)
            }
            else -> throw IllegalArgumentException("Not valid Boundary CallBack")
        }
    }

    private fun <S> setupAllGamesPagingDispatcher(
        boundaryCallBack: AllGamesBoundaryCallBack,
        livePagedList: LiveData<PagedList<S>>
    ): PagingDataListDispatcher<S> {
        val triggerRefresh = MutableLiveData<Unit>()
        val refreshState: LiveData<NetworkState> = triggerRefresh.switchMap {
            refresh(boundaryCallBack)
        }
        return PagingDataListDispatcher(
            pagedList = livePagedList,
            networkState = boundaryCallBack.networkState,
            retry = {
                boundaryCallBack.paginHelper.isAllRetryFailed()
            },
            refresh = {
                triggerRefresh.value = null
            },
            refreshState = refreshState
        )
    }

    private fun <S> setupPlatformDetailsPagingDispatcher(
        boundaryCallBack: PlatformDetailBoundaryCallBack,
        livePagedList: LiveData<PagedList<S>>
    ): PagingDataListDispatcher<S> {
        val triggerRefresh = MutableLiveData<Unit>()
        val refreshState: LiveData<NetworkState> = triggerRefresh.switchMap {
            refresh(boundaryCallBack)
        }
        return PagingDataListDispatcher(
            pagedList = livePagedList,
            networkState = boundaryCallBack.netWorkState,
            retry = {
                boundaryCallBack.helper.isAllRetryFailed()
            },
            refresh = {
                triggerRefresh.value = null
            },
            refreshState = refreshState
        )
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val INITIAL_LOAD_SIZE_HINT = PAGE_SIZE * 3
        private const val MAX_SIZE: Int = PagedList.Config.MAX_SIZE_UNBOUNDED
        fun pageListConfig(pageSize: Int) = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setMaxSize(MAX_SIZE)
            .build()
    }
}