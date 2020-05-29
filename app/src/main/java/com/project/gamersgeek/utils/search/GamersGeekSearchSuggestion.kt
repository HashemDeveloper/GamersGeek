package com.project.gamersgeek.utils.search

import android.widget.Filter
import com.project.gamersgeek.data.local.IGameResultRepo
import com.project.gamersgeek.data.local.ISuggestionRepo
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GamersGeekSearchSuggestion @Inject constructor(): IGamersGeekSearchSuggestion, CoroutineScope {
    private val job = Job()
    @Inject
    lateinit var iGameResultRepo: IGameResultRepo
    @Inject
    lateinit var suggestionRepo: ISuggestionRepo
    @Inject
    lateinit var iGamersGeekRemoteRepo: IRawgGamerGeekApiHelper


    override fun findSuggestions(query: String, limit: Int, searchSuggestionListener: SearchSuggestionListener, searchFor: String) {
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val suggestionList: MutableList<SearchResultWrapper> = arrayListOf()
                if (!(constraint == null || constraint.isEmpty()) && constraint.length >= 3) {
                    getSuggestionList(constraint)?.let { list ->
                        for (gameResult: Results in list) {
                            if (gameResult.name!!.startsWith(constraint.toString(), true)) {
                                val resultWrapper = SearchResultWrapper(0, gameResult.name!!, false, null, searchFor)
                                suggestionList.add(resultWrapper)
                                if (limit != -1 && suggestionList.size == limit) {
                                    break
                                }
                            }
                        }
                    }
                }
                val results = FilterResults()
                suggestionList.sortWith(Comparator { lhs, rhs ->
                    if (lhs.isHistory) -1 else 0
                })
                results.values = suggestionList
                results.count = suggestionList.size
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchSuggestionListener.onSearchResult(results?.values as MutableList<SearchResultWrapper>)
            }
        }.filter(query)
    }

    override fun saveSuggestion(suggestionHistory: SearchResultWrapper) {
        this.suggestionRepo.saveSearchResult(suggestionHistory)
    }

    override fun getHistory(searchFor: String): List<SearchResultWrapper>? {
        return this.suggestionRepo.getSearchHistory(searchFor)
    }

    private fun getSuggestionList(value: CharSequence): List<Results>? {
        val gameListRes: GameListRes? = this.iGamersGeekRemoteRepo.onSearchAllGames(value.toString())
        return gameListRes?.results
    }

    interface SearchSuggestionListener {
        fun onSearchResult(results: List<SearchResultWrapper>)

    }
    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}