package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorsGame(
    override var id_: Int?,
    override var slug_: String?,
    override var name_: String?,
    @SerializedName("added")
    @Expose
    var added: Int
): Parcelable, BaseBasicInfoModel(id_, slug_, name_)