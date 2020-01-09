package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rating(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("count")
    @Expose
    var count: Int,
    @SerializedName("percent")
    @Expose
    var percent: Double
): Parcelable