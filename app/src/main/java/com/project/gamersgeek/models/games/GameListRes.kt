package com.project.gamersgeek.models.games

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "list_of_games")
@Parcelize
data class GameListRes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var long: Long,
    @ColumnInfo(name = "count")
    @SerializedName("count")
    @Expose
    var count: Int,
    @ColumnInfo(name = "next")
    @SerializedName("next")
    @Expose
    var next: String,
    @ColumnInfo(name = "previous")
    @SerializedName("previous")
    @Expose
    var previous: String,
    @ColumnInfo(name = "results")
    @SerializedName("results")
    @Expose
    var results: @RawValue List<Results>? = arrayListOf(),
    @SerializedName("seo_title")
    @Expose
    var seoTitle: String,
    @SerializedName("seo_description")
    @Expose
    var seoDescription: String,
    @SerializedName("seo_keywords")
    @Expose
    var seoKeywords: String,
    @SerializedName("seo_h1")
    @Expose
    var categoryType: String,
    @SerializedName("noindex")
    @Expose
    var noIndex: Boolean,
    @SerializedName("nofollow")
    @Expose
    var noFollow: Boolean,
    @SerializedName("description")
    @Expose
    var description: String,
    @SerializedName("filters")
    @Expose
    var gameFilter: GameListFilter,
    @SerializedName("nofollow_collections")
    @Expose
    var noFollowCollection: List<String> = arrayListOf()
): Parcelable