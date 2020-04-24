package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameProfileHeader(
    var gamePlatformImage: String?,
    var platformName: String
): Parcelable