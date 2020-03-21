package com.project.gamersgeek.models.stores

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class GameStoreToBuy(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("next")
    @Expose
    val next: Int,
    @SerializedName("previous")
    @Expose
    val previous: Int,
    @SerializedName("results")
    @Expose
    val storeResults: List<StoreResult>?= arrayListOf()
): Parcelable