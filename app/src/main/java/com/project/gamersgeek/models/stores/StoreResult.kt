package com.project.gamersgeek.models.stores

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreResult(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("game_id")
    @Expose
    val gameId: Int,
    @SerializedName("store_id")
    @Expose
    val storeId: Int,
    @SerializedName("url")
    @Expose
    val url: String
): Parcelable
