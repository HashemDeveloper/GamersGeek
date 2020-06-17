package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorResults(
    @SerializedName("id")
    @Expose
    var id_: Int?,
    @SerializedName("slug")
    @Expose
    var slug_: String?,
    @SerializedName("name")
    @Expose
    var name_: String?,
    @SerializedName("image")
    @Expose
    var image_: String?= null,
    @SerializedName("image_background")
    var imageBackground: String?= null,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int?,
    @SerializedName("positions")
    @Expose
    var positionList: List<CreatorPosition> ?= arrayListOf(),
    @SerializedName("games")
    @Expose
    var gameList: List<CreatorsGame> ?= arrayListOf()
): Parcelable