package com.project.gamersgeek.models.games

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "saved_games")
@Parcelize
data class SaveGames(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var gameId: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "date")
    var date: OffsetDateTime,
    @ColumnInfo(name = "played")
    var isPlayed: Boolean,
    @ColumnInfo(name = "game_results")
    var gameResult: Results?,
    @ColumnInfo(name = "store_list")
    val storeList: List<Store>? = arrayListOf(),
    @ColumnInfo(name = "background_image")
    val backgroundImage: String,
    @ColumnInfo(name = "additional_bg_image")
    val additionalBgImage: String
): Parcelable