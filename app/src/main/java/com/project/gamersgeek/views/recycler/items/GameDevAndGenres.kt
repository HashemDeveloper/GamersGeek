package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.developer.Developer
import com.project.gamersgeek.models.games.GameGenre
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameDevAndGenres(val devList: List<Developer>?, val genresList: List<GameGenre>?): Parcelable