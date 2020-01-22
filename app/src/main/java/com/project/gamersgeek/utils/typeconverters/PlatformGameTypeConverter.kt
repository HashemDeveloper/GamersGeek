package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformGames
import java.lang.reflect.Type

class PlatformGameTypeConverter {
    @TypeConverter
    fun stringToPlatformGames(json: String): List<PlatformGames> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<PlatformGames>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun platformGamesToString(platformGames: List<PlatformGames>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<PlatformGames>>(){}.type
        return gson.toJson(platformGames, type)
    }
}