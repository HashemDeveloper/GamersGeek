package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.games.ShortScreenShot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScreenShots(val screenShotList: List<ShortScreenShot>?): Parcelable