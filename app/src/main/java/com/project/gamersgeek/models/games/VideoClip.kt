package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class VideoClip(
    @SerializedName("clip")
    @Expose
    var clip: String,
    @SerializedName("clips")
    @Expose
    var resolution: @RawValue VideoResolution,
    @SerializedName("video")
    @Expose
    var video: String,
    @SerializedName("preview")
    @Expose
    var preview: String
): Parcelable