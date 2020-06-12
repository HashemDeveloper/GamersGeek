package com.project.gamersgeek.models.base

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
open class BaseResModel <T> constructor (
    @SerializedName("count")
    @Expose
    open var count: Int?,
    @SerializedName("next")
    @Expose
    open var next: String?,
    @SerializedName("previous")
    @Expose
    open var previous: String?,
    @SerializedName("results")
    @Expose
    open var results: @RawValue List<T>?= arrayListOf()
): Parcelable