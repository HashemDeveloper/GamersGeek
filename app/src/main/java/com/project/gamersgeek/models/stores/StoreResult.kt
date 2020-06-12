package com.project.gamersgeek.models.stores

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreResult(
    override var id_: Int?,
    override var name_: String?,
    override var slug_: String?,
    override var imageBackground: String?,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int?,
    @SerializedName("domain")
    @Expose
    var domain: String?,
    @SerializedName("game_id")
    @Expose
    val gameId: Int,
    @SerializedName("store_id")
    @Expose
    val storeId: Int,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("games")
    @Expose
    var list: List<StoreGames>?= arrayListOf()
): Parcelable, BaseBasicInfoModel()
