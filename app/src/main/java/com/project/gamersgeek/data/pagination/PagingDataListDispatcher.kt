package com.project.gamersgeek.data.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.utils.paging.NetworkState

data class PagingDataListDispatcher<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)