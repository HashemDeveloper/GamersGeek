package com.project.gamersgeek.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.project.gamersgeek.models.platforms.PlatformDetails


interface IGamerGeekRepository {

    fun getPlatformDetailsPagedData(): LiveData<PagedList<PlatformDetails>>
}