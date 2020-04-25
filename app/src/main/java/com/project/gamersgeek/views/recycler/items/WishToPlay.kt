package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.games.SaveGames
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WishToPlay(
    var saveGameList: List<SaveGames>? = arrayListOf()
): Parcelable