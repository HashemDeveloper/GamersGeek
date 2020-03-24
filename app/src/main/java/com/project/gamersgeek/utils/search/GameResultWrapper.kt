package com.project.gamersgeek.utils.search

import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.project.gamersgeek.models.games.Results
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameResultWrapper(
    var searchBody: String,
    var isHistory: Boolean,
    var searchType: SearchByType
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return this.searchBody
    }
    enum class SearchByType {
        NAME,
        PLATFORM
    }
}