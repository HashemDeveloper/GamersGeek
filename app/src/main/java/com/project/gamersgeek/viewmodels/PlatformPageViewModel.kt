package com.project.gamersgeek.viewmodels

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.search.SearchHelper
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    val networkLiveData: MutableLiveData<NetworkStateEvent> = MutableLiveData()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    @Inject
    lateinit var platformDetailsDao: IPlatformDetailsDao
    @Inject
    lateinit var iGameResultDao: IGameResultDao
    @Inject
    lateinit var iSharedPrefService: ISharedPrefService
    val textFilterLiveData: MutableLiveData<SearchHelper> = MutableLiveData()
    private var searchResultLiveData: LiveData<PagedList<PlatformDetails>>?= null
    private var openDrawer: Boolean?= null
    private var drawerLayout: DrawerLayout?= null
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
            } else if (getGameResultBlocked() != null) {
                val jobGameResults: Deferred<Results> = async { getGameResultBlocked()!! }
                val results: Results? = jobGameResults.await()
                if (results != null) {
                    backgroundImage = results.backgroundImage!!
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
    private fun getGameResultBlocked(): Results? {
        var gameResult: Results?= null
        runBlocking {
            if (getGameResultDao() != null) {
                val jobGameResult: Deferred<Results> = async { getGameResultDao()!! }
                gameResult = jobGameResult.await()
            }
        }
        return gameResult
    }
    private suspend fun getGameResultDao(): Results? {
        return this.iGameResultDao.getGameResult()
    }
    private suspend fun getPlatformDetails(): PlatformDetails? {
        return platformDetailsDao.getPlatformDetails()
    }

    fun openDrawer() {
        Constants.toggleDrawer(this.drawerLayout)
    }

    fun setupNetConnection(networkStateEvent: NetworkStateEvent) {
        this.networkLiveData.value = networkStateEvent
    }

    fun getIsNightModeOn(): Boolean {
        return this.iSharedPrefService.getIsNightModeOn()
    }

    fun findSuggestions(newQuery: String?, platformPageSearchId: FloatingSearchView) {
        platformPageSearchId.showProgress()
        // TODO: Create suggestions for platform information query
    }

    fun onSearch(searchHelper: SearchHelper) {
        this.textFilterLiveData.value = searchHelper
        this.searchResultLiveData = Transformations.switchMap(this.textFilterLiveData) { input ->
            platformDetailsList.search(input)
        }
    }

    fun setupNavDrawer(drawerLayout: DrawerLayout?) {
        this.drawerLayout = drawerLayout
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}