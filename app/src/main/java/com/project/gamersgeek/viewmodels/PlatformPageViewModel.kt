package com.project.gamersgeek.viewmodels

import androidx.lifecycle.*
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
//    val mediatorLiveData: MediatorLiveData<PagedList<PlatformDetails>> = MediatorLiveData()
    val networkLiveData: MutableLiveData<NetworkStateEvent> = MutableLiveData()
    private val mLock = Any()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    @Inject
    lateinit var platformDetailsDao: IPlatformDetailsDao
    private val job = Job()

    private val platformDetailsList by lazy {
          this.iGamerGeekRepository.getPlatformDetailsPagedData(50)
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
    fun getNavBackgroundImage(): String {
        var backgroundImage: String?= null
        runBlocking {
            val jobBackgroundImage: Deferred<String> = async { getBackgroundImage() }
            backgroundImage = jobBackgroundImage.await()
        }
        return backgroundImage!!
    }
    private fun getBackgroundImage(): String {
        var backgroundImage = ""
        runBlocking {
            if (getPlatformDetailsLocked() != null) {
                val platformDetails: Deferred<PlatformDetails> = async { getPlatformDetailsLocked()!! }
                val details: PlatformDetails? = platformDetails.await()
                if (details != null) {
                    backgroundImage = details.imageBackground
                }
            }
        }
        return backgroundImage
    }
    private fun getPlatformDetailsLocked(): PlatformDetails? {
        var platformDetails: PlatformDetails?= null
        runBlocking {
            if (getPlatformDetails() != null) {
                val jobPlatformDetails: Deferred<PlatformDetails> = async { getPlatformDetails()!! }
                platformDetails = jobPlatformDetails.await()
            }
        }
        return platformDetails
    }
    private suspend fun getPlatformDetails(): PlatformDetails? {
        return platformDetailsDao.getPlatformDetails()
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