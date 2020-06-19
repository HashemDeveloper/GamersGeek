package com.project.gamersgeek.data.local

import com.project.gamersgeek.utils.search.SearchResultWrapper

interface ISuggestionRepo {
    fun saveSearchResult(suggestionHistory: SearchResultWrapper)
    fun getSearchHistory(searchFor: String): List<SearchResultWrapper>?
    fun deleteSearchHistory(searchFor: String)
    fun deleteByName(search: String)
    fun deleteOldHistory(searchFor: String)
    fun deleteOldRecords(expiredTime: Long)
}