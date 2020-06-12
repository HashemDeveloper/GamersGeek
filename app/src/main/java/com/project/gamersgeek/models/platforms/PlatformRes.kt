package com.project.gamersgeek.models.platforms

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.base.BaseResModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlatformRes(
    override var count: Int?,
    override var next: String?,
    override var previous: String?,
    override var results: List<PlatformDetails>?= arrayListOf()
): Parcelable, BaseResModel<PlatformDetails>(count, next, previous, results)