package com.project.gamersgeek.views.recycler.items

import android.os.Parcelable
import com.project.gamersgeek.models.games.Store
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameDetailsFooter(
    val id: Int,
    val name: String?,
    val websiteUrl: String?,
    val esrbRating: String?,
    val storeList: List<Store>?,
    val backgroundImage1: String,
    val backgroundImage2: String): Parcelable