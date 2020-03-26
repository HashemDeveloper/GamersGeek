package com.project.gamersgeek.data.local

import com.project.gamersgeek.utils.search.GameResultWrapper

interface ISuggestionRepo {
    fun saveSearchResult(suggestionHistory: GameResultWrapper)
    fun getSearchHistory(): List<GameResultWrapper>?
    fun deleteSearchHistory()
    fun deleteByName(search: String)
    fun deleteOldHistory()
}