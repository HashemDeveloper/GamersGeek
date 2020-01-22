package com.project.gamersgeek.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.gamersgeek.models.platforms.PlatformDetails
import javax.inject.Inject

class PlatformDetailsDataSourceFactory @Inject constructor(private val iPlatformDetailsDao: IPlatformDetailsDao,
                                                           private val iRawgGameDbApiHelper: IRawgGameDbApiHelper): DataSource.Factory<Int, PlatformDetails>() {

    private val liveData: MutableLiveData<PlatformDetailsDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, PlatformDetails> {
        val source = PlatformDetailsDataSource(this.iPlatformDetailsDao, this.iRawgGameDbApiHelper)
        this.liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 20

        fun pageListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}