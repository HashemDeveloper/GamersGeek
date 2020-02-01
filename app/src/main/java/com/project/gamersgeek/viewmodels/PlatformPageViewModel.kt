package com.project.gamersgeek.viewmodels

import androidx.lifecycle.ViewModel
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.events.HamburgerEvent
import com.project.neardoc.rxeventbus.IRxEventBus
import javax.inject.Inject

class PlatformPageViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    val fetchGamePlatforms by lazy {
        this.iGamerGeekRepository.getPlatformDetailsPagedData()
    }

    fun refresh() {
        this.fetchGamePlatforms.value?.dataSource?.invalidate()
    }

    fun setupDrawer(platformPageSearchId: FloatingSearchView?) {
        this.iRxEventBus.post(HamburgerEvent(platformPageSearchId!!))
    }
}