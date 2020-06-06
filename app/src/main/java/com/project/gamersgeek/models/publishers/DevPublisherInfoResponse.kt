package com.project.gamersgeek.models.publishers

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DevPublisherInfoResponse(
    @SerializedName("count")
    @Expose
    var count: Int,
    @SerializedName("next")
    @Expose
    var next: String,
    @SerializedName("previous")
    @Expose
    var previous: String,
    @SerializedName("results")
    @Expose
    var resultList: List<PublisherResult> = arrayListOf()
): Parcelable