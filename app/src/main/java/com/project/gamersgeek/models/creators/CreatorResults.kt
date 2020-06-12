package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorResults(
    override var id: Int?,
    override var slug: String?,
    override var name: String?,
    override var image: String?= null,
    override var imageBackground: String?= null,
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int?,
    @SerializedName("positions")
    @Expose
    var positionList: List<CreatorPosition> ?= arrayListOf(),
    @SerializedName("games")
    @Expose
    var gameList: List<CreatorsGame> ?= arrayListOf()
): Parcelable, BaseBasicInfoModel(id, slug, name)