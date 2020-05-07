package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Store(
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("store")
    @Expose
    var storeList: @RawValue StoreList?,
    @SerializedName("url_en")
    @Expose
    var englishUrl: String?,
    @SerializedName("url_ru")
    @Expose
    var russianUrl: String?
): Parcelable