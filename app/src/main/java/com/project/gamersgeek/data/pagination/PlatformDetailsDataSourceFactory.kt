package com.project.gamersgeek.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.remote.IRawgGameDbApi
import com.project.gamersgeek.models.platforms.PlatformDetails

class PlatformDetailsDataSourceFactory (private val iPlatformDetailsDao: IPlatformDetailsDao,
                                                           private val iRawgGameDbApi: IRawgGameDbApi): DataSource.Factory<Int, PlatformDetails>() {

    private val liveData: MutableLiveData<PlatformDetailsDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, PlatformDetails> {
        val source = PlatformDetailsDataSource(this.iPlatformDetailsDao, this.iRawgGameDbApi)
        this.liveData.postValue(source)
        return source
    }
}