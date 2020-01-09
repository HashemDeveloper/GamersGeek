package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlatformDetails(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("image")
    @Expose
    var image: String,
    @SerializedName("year_end")
    @Expose
    var yearEnd: String?,
    @SerializedName("year_start")
    @Expose
    var yearStart: String?,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int,
    @SerializedName("image_background")
    @Expose
    var imageBackground: String
): Parcelable