package com.project.gamersgeek.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.pagination.PlatformDetailsDataSource
import com.project.gamersgeek.data.pagination.PlatformDetailsDataSourceFactory
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import javax.inject.Inject

class GamerGeekRepository @Inject constructor(): IGamerGeekRepository {
    @Inject
    lateinit var iRawgGameDbApiHelper: IRawgGameDbApiHelper
    @Inject
    lateinit var iPlatformDetailsDao: IPlatformDetailsDao

    override fun getPlatformDetailsPagedData(): LiveData<PagedList<PlatformDetails>> {
        val dataSourceFactory = PlatformDetailsDataSourceFactory(this.iPlatformDetailsDao, this.iRawgGameDbApiHelper)
        return LivePagedListBuilder(dataSourceFactory, PlatformDetailsDataSourceFactory.pageListConfig()).build()
    }
}