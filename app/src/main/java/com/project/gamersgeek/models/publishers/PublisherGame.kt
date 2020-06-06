package com.project.gamersgeek.models.publishers

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PublisherGame(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("added")
    @Expose
    var added: Int
): Parcelable