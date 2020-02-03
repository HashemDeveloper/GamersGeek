package com.project.gamersgeek.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.pagination.PlatformDetailBoundaryCallBack
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.platforms.PlatformDetails
import javax.inject.Inject

class GamerGeekRepository @Inject constructor(): IGamerGeekRepository {
    @Inject
    lateinit var iRawgGameDbApi: IRawgGameDbApi
    @Inject
    lateinit var iPlatformDetailsDao: IPlatformDetailsDao

    override fun getPlatformDetailsPagedData(): LiveData<PagedList<PlatformDetails>> {
       return getPlatformDetailsFromLocalDb()
    }

    private fun getPlatformDetailsFromLocalDb(): LiveData<PagedList<PlatformDetails>> {
        val dataSourceFactory: DataSource.Factory<Int, PlatformDetails> = this.iPlatformDetailsDao.getPlatformDetails()
        val platformDetailBoundaryCallBack = PlatformDetailBoundaryCallBack(this.iPlatformDetailsDao, this.iRawgGameDbApi)
        return LivePagedListBuilder(dataSourceFactory, pageListConfig())
            .setBoundaryCallback(platformDetailBoundaryCallBack)
            .build()
    }

    companion object {
        private const val PAGE_SIZE = 10

        fun pageListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}