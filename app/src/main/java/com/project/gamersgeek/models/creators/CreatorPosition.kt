package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.project.gamersgeek.models.base.BaseBasicInfoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorPosition(
    override var id: Int?,
    override var slug: String?,
    override var name: String?
): Parcelable, BaseBasicInfoModel(id, slug, name)