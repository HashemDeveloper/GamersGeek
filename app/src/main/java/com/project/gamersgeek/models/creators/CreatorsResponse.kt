package com.project.gamersgeek.models.creators

import android.os.Parcelable
import com.project.gamersgeek.models.base.BaseResModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorsResponse(
    override var count: Int?,
    override var next: String?,
    override var previous: String?,
    override var results: List<CreatorResults>?= arrayListOf()
): Parcelable, BaseResModel<CreatorResults>(count, next, previous, results)