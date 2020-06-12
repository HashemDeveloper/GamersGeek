package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorsGame(
    override var id: Int?,
    override var slug: String?,
    override var name: String?,
    @SerializedName("added")
    @Expose
    var added: Int
): Parcelable, BaseBasicInfoModel(id, slug, name)