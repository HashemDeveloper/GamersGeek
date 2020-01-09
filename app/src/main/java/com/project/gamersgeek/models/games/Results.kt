package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Results(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("released")
    @Expose
    var released: String,
    @SerializedName("tba")
    @Expose
    var tba: Boolean,
    @SerializedName("background_image")
    @Expose
    var backgroundImage: String,
    @SerializedName("rating")
    @Expose
    var rating: Double,
    @SerializedName("rating_top")
    @Expose
    var ratingTop: Int,
    @SerializedName("ratings")
    @Expose
    var ratingList: @RawValue List<Rating>?= arrayListOf(),
    @SerializedName("ratings_count")
    @Expose
    var ratingCount: Int,
    @SerializedName("reviews_text_count")
    @Expose
    var reviewsTextCount: Int,
    @SerializedName("added")
    @Expose
    var added: Int,
    @SerializedName("added_by_status")
    @Expose
    var addedByStatus: @RawValue AddedByStatus,
    @SerializedName("metacritic")
    @Expose
    var metaCritic: Int,
    @SerializedName("playtime")
    @Expose
    var playTime: Long,
    @SerializedName("suggestions_count")
    @Expose
    var suggestionsCount: Int,
    @SerializedName("reviews_count")
    @Expose
    var reviewsCount: Int,
    @SerializedName("saturated_color")
    @Expose
    var saturatedColor: String,
    @SerializedName("dominant_color")
    @Expose
    var dominentColor: String,
    @SerializedName("platforms")
    @Expose
    var platformList: @RawValue List<CategorizedGamePlatforms>?= arrayListOf(),
    @SerializedName("parent_platforms")
    @Expose
    var genericPlatformList: @RawValue List<GameGenericPlatform>?= arrayListOf(),
    @SerializedName("genres")
    @Expose
    var genresList: @RawValue List<GameGenre>?= arrayListOf(),
    @SerializedName("stores")
    @Expose
    var listOfStores: @RawValue List<Store>?= arrayListOf(),
    @SerializedName("clip")
    @Expose
    var videoClip: @RawValue VideoClip,
    @SerializedName("short_screenshots")
    @Expose
    var shortScreenShotList: @RawValue List<ShortScreenShot>?= arrayListOf()
): Parcelable