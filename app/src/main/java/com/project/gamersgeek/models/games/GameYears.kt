package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.games.GameYearList
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GameYears(
    @SerializedName("from")
    @Expose
    var from: Int,
    @SerializedName("to")
    @Expose
    var to: Int,
    @SerializedName("filter")
    @Expose
    var filter: String,
    @SerializedName("decade")
    @Expose
    var decade: Int,
    @SerializedName("years")
    @Expose
    var gameYearList: @RawValue List<GameYearList>?= arrayListOf(),
    @SerializedName("nofollow")
    @Expose
    var noFollow: Boolean,
    @SerializedName("count")
    @Expose
    var count: Int
): Parcelable