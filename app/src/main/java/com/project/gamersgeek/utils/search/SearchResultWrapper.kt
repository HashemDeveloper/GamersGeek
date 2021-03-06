package com.project.gamersgeek.utils.search

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.project.gamersgeek.models.games.Results
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "suggestion_history", indices = [Index(value = ["suggestion"], unique = true)])
@Parcelize
data class SearchResultWrapper(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "suggestion")
    var searchBody: String,
    @ColumnInfo(name = "is_history")
    var isHistory: Boolean,
    @ColumnInfo(name = "date")
    var date: OffsetDateTime?,
    @ColumnInfo(name = "time_ms")
    var timeMs:Long?,
    @ColumnInfo(name = "searchFor")
    var searchFor: String
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return this.searchBody
    }
    enum class SearchByType {
        NAME,
        PLATFORM
    }
}