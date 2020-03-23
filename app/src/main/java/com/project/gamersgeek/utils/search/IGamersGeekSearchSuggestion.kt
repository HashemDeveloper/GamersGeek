package com.project.gamersgeek.utils.search

interface IGamersGeekSearchSuggestion {
    fun findSuggestions(query: String, limit: Int, searchSuggestionListener: GamersGeekSearchSuggestion.SearchSuggestionListener)
}