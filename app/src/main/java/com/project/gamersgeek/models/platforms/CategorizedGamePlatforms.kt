package com.project.gamersgeek.models.platforms

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.games.Requirements
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CategorizedGamePlatforms(
    @SerializedName("platform")
    @Expose
    var platformDetails: @RawValue PlatformDetails,
    @SerializedName("released_at")
    @Expose
    var releasedAt: String,
    @SerializedName("requirements_en")
    @Expose
    var requirementsInEnglish: @RawValue List<Requirements>?= arrayListOf(),
    @SerializedName("requirements_ru")
    @Expose
    var requirementsInRussian: @RawValue List<Requirements>?= arrayListOf()
): Parcelable