package com.project.gamersgeek.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.project.gamersgeek.data.pagination.PagingDataListDispatcher
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.PlatformDetails


interface IGamerGeekRepository {

    fun getPlatformDetailsPagedData(pageSize: Int): PagingDataListDispatcher<PlatformDetails>

    fun getAllGamesPagedData(pageSize: Int): PagingDataListDispatcher<Results>
}