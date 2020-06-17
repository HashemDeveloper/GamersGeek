package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorsGame(
    @SerializedName("id")
    @Expose
    var id_: Int?,
    @SerializedName("slug")
    @Expose
    var slug_: String?,
    @SerializedName("name")
    @Expose
    var name_: String?,
    @SerializedName("added")
    @Expose
    var added: Int
): Parcelable