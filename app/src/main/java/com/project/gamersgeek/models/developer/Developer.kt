package com.project.gamersgeek.models.developer

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Developer(
    override var id_: Int?,
    override var name_: String?,
    override var slug_: String?,
    override var imageBackground: String?,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int
): Parcelable, BaseBasicInfoModel()