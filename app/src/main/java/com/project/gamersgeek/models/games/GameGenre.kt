package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameGenre(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int,
    @SerializedName("image_background")
    @Expose
    var imageBackground: String
): Parcelable