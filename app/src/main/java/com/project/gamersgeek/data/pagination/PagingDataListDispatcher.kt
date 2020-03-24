package com.project.gamersgeek.data.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.paging.NetworkState
import com.project.gamersgeek.utils.search.SearchHelper

data class PagingDataListDispatcher<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val search: (search: SearchHelper) -> LiveData<PagedList<Results>>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)