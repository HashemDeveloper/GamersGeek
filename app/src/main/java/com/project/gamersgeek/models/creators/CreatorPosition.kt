package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorPosition(
    override var id_: Int?,
    override var slug_: String?,
    override var name_: String?
): Parcelable, BaseBasicInfoModel(id_, slug_, name_)