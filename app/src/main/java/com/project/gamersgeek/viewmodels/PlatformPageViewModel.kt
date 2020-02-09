package com.project.gamersgeek.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.pagination.PagingDataListDispatcher
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
//    val mediatorLiveData: MediatorLiveData<PagedList<PlatformDetails>> = MediatorLiveData()
    val networkLiveData: MutableLiveData<NetworkStateEvent> = MutableLiveData()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    @Inject
    lateinit var platformDetailsDao: IPlatformDetailsDao
    private val job = Job()

//    val remotePlatformDetailsLiveData by lazy {
//        this.iGamerGeekRepository.getPlatformDetailsPagedData(this.isNetConnected)
//    }
    private val platformDetailsList by lazy {
          this.iGamerGeekRepository.getPlatformDetailsPagedData()
    }

    val platformDetailsLiveData by lazy {
        this.platformDetailsList.pagedList
    }
    val refreshState by lazy {
        this.platformDetailsList.refreshState
    }
    val networkState by lazy {
        this.platformDetailsList.networkState
    }

    fun refresh() {
        this.platformDetailsList.refresh.invoke()
    }
    fun retry() {
        this.platformDetailsList.retry.invoke()
    }
    fun getNavBackgroundImage() {
//        launch {
//            val platformDetails: PlatformDetails = platformDetailsDao.getPlatformDetails()
//            platformDetails.let {
//                Timber.e("Name: ${it.name}")
//            }
//        }
    }

    fun setupDrawer(platformPageSearchId: FloatingSearchView?) {
        this.iRxEventBus.post(HamburgerEvent(platformPageSearchId!!))
    }

    fun setupNetConnection(networkStateEvent: NetworkStateEvent) {
        this.networkLiveData.value = networkStateEvent
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}