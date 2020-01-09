package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoResolution(
    @SerializedName("320")
    @Expose
    var res_320: String,
    @SerializedName("640")
    @Expose
    var res_640: String,
    @SerializedName("full")
    @Expose
    var res_full: String
): Parcelable