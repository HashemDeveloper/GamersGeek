package com.project.gamersgeek.utils.search

interface IGamersGeekSearchSuggestion {
    fun findSuggestions(query: String, limit: Int, searchSuggestionListener: GamersGeekSearchSuggestion.SearchSuggestionListener, searchFor: String)
    fun saveSuggestion(suggestionHistory: SearchResultWrapper)
    fun getHistory(searchFor: String): List<SearchResultWrapper>?
}