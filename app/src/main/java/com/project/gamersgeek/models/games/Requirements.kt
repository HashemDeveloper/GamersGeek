package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Requirements(
    @SerializedName("minimum")
    @Expose
    var minimium: String,
    @SerializedName("recommended")
    @Expose
    var recommended: String
): Parcelable