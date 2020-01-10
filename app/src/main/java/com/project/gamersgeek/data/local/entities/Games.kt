package com.project.gamersgeek.data.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "games")
data class Games(
    @PrimaryKey
    @ColumnInfo(name = "games_id")
    var gamesId: String
): Parcelable