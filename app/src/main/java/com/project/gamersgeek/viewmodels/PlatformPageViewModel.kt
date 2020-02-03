package com.project.gamersgeek.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.neardoc.rxeventbus.IRxEventBus
import javax.inject.Inject

class PlatformPageViewModel @Inject constructor(): ViewModel() {
    val mediatorLiveData: MediatorLiveData<PagedList<PlatformDetails>> = MediatorLiveData()
    val networkLiveData: MutableLiveData<NetworkStateEvent> = MutableLiveData()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    lateinit var remotePlatformDetailsLiveData: LiveData<PagedList<PlatformDetails>>
//    val remotePlatformDetailsLiveData by lazy {
//        this.iGamerGeekRepository.getPlatformDetailsPagedData(this.isNetConnected)
//    }

    fun refresh() {
        if (::remotePlatformDetailsLiveData.isInitialized) {
            this.mediatorLiveData.value?.dataSource?.invalidate()
        } else {
            this.remotePlatformDetailsLiveData = this.iGamerGeekRepository.getPlatformDetailsPagedData()
            this.mediatorLiveData.addSource(this.remotePlatformDetailsLiveData) {
                this.mediatorLiveData.value = it
            }
        }
    }

    fun setupDrawer(platformPageSearchId: FloatingSearchView?) {
        this.iRxEventBus.post(HamburgerEvent(platformPageSearchId!!))
    }

    fun setupNetConnection(networkStateEvent: NetworkStateEvent) {
        this.networkLiveData.value = networkStateEvent
    }
}