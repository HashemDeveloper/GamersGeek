package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddedByStatus(
    @SerializedName("yet")
    @Expose
    var yet: Int,
    @SerializedName("owned")
    @Expose
    var owned: Int,
    @SerializedName("beaten")
    @Expose
    var beaten: Int,
    @SerializedName("toplay")
    @Expose
    var toPlay: Int,
    @SerializedName("dropped")
    @Expose
    var dropped: Int,
    @SerializedName("playing")
    @Expose
    var playing: Int
): Parcelable