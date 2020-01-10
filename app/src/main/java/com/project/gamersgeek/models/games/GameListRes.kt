package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GameListRes(
    @SerializedName("count")
    @Expose
    var count: Int,
    @SerializedName("next")
    @Expose
    var next: String,
    @SerializedName("previous")
    @Expose
    var previous: String,
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