package com.project.gamersgeek.utils.search

import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameResultWrapper(
    var searchBody: String,
    var isHistory: Boolean
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return this.searchBody
    }
}