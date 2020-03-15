package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PcRequirements(val platformList: List<CategorizedGamePlatforms>): Parcelable