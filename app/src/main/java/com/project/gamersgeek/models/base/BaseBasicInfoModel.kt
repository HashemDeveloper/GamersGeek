package com.project.gamersgeek.models.base

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseBasicInfoModel constructor(
    @SerializedName("id") @Expose open var id_: Int?=null,
    @SerializedName("slug") @Expose open var slug_: String?=null,
    @SerializedName("name") @Expose open var name_: String?=null,
    @SerializedName("image") @Expose open var image_: String?= null,
    @SerializedName("image_background") @Expose open var imageBackground: String?=null,
    @SerializedName("description") @Expose open var description_: String?=null
): Parcelable