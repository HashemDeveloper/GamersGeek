package com.project.gamersgeek.utils.search

import android.content.Context
import android.widget.Filter
import com.project.gamersgeek.data.local.IGameResultRepo
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

    override fun findSuggestions(query: String, limit: Int, searchSuggestionListener: SearchSuggestionListener) {
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val suggestionList: MutableList<GameResultWrapper> = arrayListOf()
                if (!(constraint == null || constraint.isEmpty())) {
                    getSuggestionList()?.let { list ->
                        for (gameResult: Results in list) {
                            if (gameResult.name.startsWith(constraint.toString(), true)) {
                                val resultWrapper = GameResultWrapper(gameResult.name, false)
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
                searchSuggestionListener.onSearchResult(results?.values as MutableList<GameResultWrapper>)
            }
        }.filter(query)
    }

    private fun getSuggestionList(): List<Results>? {
        return this.iGameResultRepo.getAllGameResult();
    }

    interface SearchSuggestionListener {
        fun onSearchResult(results: List<GameResultWrapper>)

    }
    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}