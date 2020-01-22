package com.project.gamersgeek.models.platforms

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "platform_details")
@Parcelize
data class PlatformDetails(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    var name: String,
    @ColumnInfo(name = "slug")
    @SerializedName("slug")
    @Expose
    var slug: String,
    @ColumnInfo(name = "image")
    @SerializedName("image")
    @Expose
    var image: String?,
    @ColumnInfo(name = "year_end")
    @SerializedName("year_end")
    @Expose
    var yearEnd: String?,
    @ColumnInfo(name = "year_start")
    @SerializedName("year_start")
    @Expose
    var yearStart: String?,
    @ColumnInfo(name = "games_count")
    @SerializedName("games_count")
    @Expose
    var gamesCount: Int,
    @ColumnInfo(name = "image_background")
    @SerializedName("image_background")
    @Expose
    var imageBackground: String,
    @ColumnInfo(name = "games")
    @SerializedName("games")
    @Expose
    var games: List<PlatformGames> = arrayListOf(),
    @ColumnInfo(name = "following")
    @SerializedName("following")
    @Expose
    var following : Boolean
): Parcelable