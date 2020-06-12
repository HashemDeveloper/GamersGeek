package com.project.gamersgeek.models.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class BaseResModel <T> constructor (
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
    open var results: List<T>?= arrayListOf()
)