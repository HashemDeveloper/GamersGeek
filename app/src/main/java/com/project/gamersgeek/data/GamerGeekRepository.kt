package com.project.gamersgeek.data

import android.annotation.SuppressLint
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
import com.project.gamersgeek.utils.search.GameResultWrapper
import com.project.gamersgeek.utils.search.SearchHelper
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

    @SuppressLint("RestrictedApi")
    private fun getPlatformDetailsFromLocalDb(pageSize: Int): PagingDataListDispatcher<PlatformDetails> {
        val dataSourceFactory: DataSource.Factory<Int, PlatformDetails> = this.iPlatformDetailsDao.getAllPlatformDetails()
        val platformDetailBoundaryCallBack = PlatformDetailBoundaryCallBack(this.iPlatformDetailsDao, this.iRawgGameDbApi)
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        val livePagedList: LiveData<PagedList<PlatformDetails>> = LivePagedListBuilder(dataSourceFactory, pageListConfig(pageSize, PLATFORM_DETAILS_INITIAL_LOAD_SIZE))
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

    @SuppressLint("RestrictedApi")
    private fun processAllGameListData(pageSize: Int): PagingDataListDispatcher<Results> {
        val allGameDataSource: DataSource.Factory<Int, Results> = this.iGameResultDao.getAllGameResultForDatasource()
        val allGamesBoundaryCallBack = AllGamesBoundaryCallBack(this.iGameResultDao, this.iRawgGameDbApi)
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        val livePagedList: LiveData<PagedList<Results>> = LivePagedListBuilder(allGameDataSource, pageListConfig(pageSize, ALL_GAMES_INITIAL_LOAD_SIZE))
            .setBoundaryCallback(allGamesBoundaryCallBack)
            .setFetchExecutor(fetchExecutor)
            .setInitialLoadKey(50)
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
            search = {
                onSearch(it)
            },
            refresh = {
                triggerRefresh.value = null
            },
            refreshState = refreshState
        )
    }

    @SuppressLint("RestrictedApi")
    private fun onSearch(it: SearchHelper): LiveData<PagedList<Results>> {
        val selectGameDataSource: DataSource.Factory<Int, Results> = when (it.searchByType) {
            GameResultWrapper.SearchByType.NAME -> {
                this.iGameResultDao.getGamesByName(it.searchBody)
            }
            GameResultWrapper.SearchByType.PLATFORM -> {
                this.iGameResultDao.getGamesByName(it.searchBody)
            }
        }
        val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        return LivePagedListBuilder(selectGameDataSource, pageListConfig(30, ALL_GAMES_INITIAL_LOAD_SIZE))
            .setFetchExecutor(fetchExecutor)
            .build()
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
            search = {
                onSearch(it)
            },
            refresh = {
                triggerRefresh.value = null
            },
            refreshState = refreshState
        )
    }

    companion object {
        private const val ALL_GAMES_PAGE_SIZE = 50
        private const val PLATFORM_DETAILS_PAGE_SIZE = 10
        private const val PLATFORM_DETAILS_INITIAL_LOAD_SIZE = PLATFORM_DETAILS_PAGE_SIZE * 3
        private const val ALL_GAMES_INITIAL_LOAD_SIZE = ALL_GAMES_PAGE_SIZE * 3
        private const val MAX_SIZE: Int = PagedList.Config.MAX_SIZE_UNBOUNDED
        fun pageListConfig(pageSize: Int, initialPageLoadCount: Int) = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(initialPageLoadCount)
            .setMaxSize(MAX_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}