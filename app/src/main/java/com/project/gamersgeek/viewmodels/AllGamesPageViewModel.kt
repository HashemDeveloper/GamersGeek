package com.project.gamersgeek.viewmodels

import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AllGamesPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    private val job = Job()

    private val gameResultList by lazy {
        this.iGamerGeekRepository.getAllGamesPagedData(50)
    }
    val gameResultLiveData by lazy {
        gameResultList.pagedList
    }
    val gameResultRefreshState by lazy {
        gameResultList.refreshState
    }

    fun refresh() {
        this.gameResultList.refresh.invoke()
    }
    fun retry () {
        this.gameResultList.retry.invoke()
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}