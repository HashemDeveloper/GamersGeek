package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameYearList(
    @SerializedName("year")
    @Expose
    var year: Int,
    @SerializedName("count")
    @Expose
    var count: Int,
    @SerializedName("nofollow")
    @Expose
    var noFollow: Boolean
): Parcelable