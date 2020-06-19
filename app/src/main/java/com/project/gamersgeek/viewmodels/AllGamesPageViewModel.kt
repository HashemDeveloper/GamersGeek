package com.project.gamersgeek.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.search.GamersGeekSearchSuggestion
import com.project.gamersgeek.utils.search.IGamersGeekSearchSuggestion
import com.project.gamersgeek.utils.search.SearchHelper
import com.project.gamersgeek.utils.search.SearchResultWrapper
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.threeten.bp.OffsetDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class AllGamesPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var iSharedPrefService: ISharedPrefService
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository
    @Inject
    lateinit var searchSuggestion: IGamersGeekSearchSuggestion

    val textFilterLiveData: MutableLiveData<SearchHelper> = MutableLiveData()
    private var resultLiveData: LiveData<PagedList<Results>>?= null
    private val job = Job()
    private val gameResultList by lazy {
        this.iGamerGeekRepository.getAllGamesPagedData(50)
    }

    val gameResultLiveData by lazy {
        gameResultList.pagedList
    }
    val refreshState by lazy {
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

    fun getIsNightModeOn(): Boolean {
        return this.iSharedPrefService.getIsNightModeOn()
    }

    fun registerSharedPrefListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.iSharedPrefService.registerOnSharedPrefListener(listener)
    }

    fun unRegisterSharedPrefListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.iSharedPrefService.unregisterOnSharedPrefListener(listener)
    }

    fun findSuggestions(query: String, searchView: FloatingSearchView?) {
        searchView?.showProgress()
        this.searchSuggestion.findSuggestions(query, 5, object : GamersGeekSearchSuggestion.SearchSuggestionListener{
            override fun onSearchResult(results: List<SearchResultWrapper>) {
                searchView?.swapSuggestions(results)
                searchView?.hideProgress()
            }
        }, Constants.SEARCH_FOR_ALL_GAMES)
    }

    fun onSearch(searchHelper: SearchHelper) {
        this.textFilterLiveData.value = searchHelper
        this.resultLiveData = this.gameResultLiveData
        resultLiveData = Transformations.switchMap(this.textFilterLiveData) {input ->
            this.gameResultList.search(input)
        }
        saveSearchResult(searchHelper)
    }

    private fun saveSearchResult(searchHelper: SearchHelper) {
        val expiredTimeMs: Long = Constants.getExpirationTime(Constants.Companion.ExpirationType.MINUTES, 1)
        val date: OffsetDateTime = Constants.getCurrentTime()
        val suggestionHistory = SearchResultWrapper(0, searchHelper.searchBody, true, date, expiredTimeMs, Constants.SEARCH_FOR_ALL_GAMES)
        this.searchSuggestion.saveSuggestion(suggestionHistory)
    }

    fun getResultLiveData(): LiveData<PagedList<Results>>? {
        return this.resultLiveData
    }

    fun setupSearchHistory(allGameSearchViewId: FloatingSearchView?) {
        val suggestionHistories: List<SearchResultWrapper>? = this.searchSuggestion.getHistory(Constants.SEARCH_FOR_ALL_GAMES)
        suggestionHistories?.let { historyList ->
            allGameSearchViewId?.swapSuggestions(historyList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}