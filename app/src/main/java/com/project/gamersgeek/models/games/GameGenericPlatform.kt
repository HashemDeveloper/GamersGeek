package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GameGenericPlatform(
    @SerializedName("platform")
    var genericPlatformType: @RawValue GenericPlatformDetails
): Parcelable