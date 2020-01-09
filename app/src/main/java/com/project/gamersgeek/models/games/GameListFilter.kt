package com.project.gamersgeek.models.games

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GameListFilter(
    var listOfYears: @RawValue List<GameYears> = arrayListOf()
): Parcelable