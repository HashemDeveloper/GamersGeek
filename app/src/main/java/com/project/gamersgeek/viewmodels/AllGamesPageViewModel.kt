package com.project.gamersgeek.viewmodels

import androidx.lifecycle.ViewModel
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AllGamesPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var iRxEventBus: IRxEventBus
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

    fun setupDrawer(platformPageSearchId: FloatingSearchView?) {
        this.iRxEventBus.post(HamburgerEvent(platformPageSearchId!!))
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}