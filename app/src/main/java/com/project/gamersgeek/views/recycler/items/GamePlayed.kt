package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.games.SaveGames
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GamePlayed(
    var saveGameList: List<SaveGames>? = arrayListOf()
): Parcelable