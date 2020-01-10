package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShortScreenShot(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("image")
    @Expose
    var image: String
): Parcelable