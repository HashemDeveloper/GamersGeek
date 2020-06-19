package com.project.gamersgeek.utils.search

import com.project.gamersgeek.utils.Constants

interface IGamersGeekSearchSuggestion {
    fun findSuggestions(query: String, limit: Int, searchSuggestionListener: GamersGeekSearchSuggestion.SearchSuggestionListener, searchFor: String)
    fun saveSuggestion(suggestionHistory: SearchResultWrapper)
    fun getHistory(searchFor: String): List<SearchResultWrapper>?
    fun removeOldSuggestionHistory(type: Constants.Companion.ExpirationType, expiredTime: Long)
}