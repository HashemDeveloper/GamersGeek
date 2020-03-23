package com.project.gamersgeek.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.IGameResultRepo
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.search.GameResultWrapper
import com.project.gamersgeek.utils.search.GamersGeekSearchSuggestion
import com.project.gamersgeek.utils.search.IGamersGeekSearchSuggestion
import com.project.gamersgeek.views.AllGamesPage
import com.project.neardoc.rxeventbus.IRxEventBus
import kotlinx.coroutines.*
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
            override fun onSearchResult(results: List<GameResultWrapper>) {
                searchView?.swapSuggestions(results)
                searchView?.hideProgress()
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}