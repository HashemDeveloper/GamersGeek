package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.platforms.GameGenericPlatform
import java.lang.reflect.Type

class GameGenericPlatformConverter {
    @TypeConverter
    fun stringToGameGenericPlatform(json: String): List<GameGenericPlatform> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<GameGenericPlatform>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun gameGenericPlatform(gameGenericPlatformList: List<GameGenericPlatform>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<GameGenericPlatform>>(){}.type
        return gson.toJson(gameGenericPlatformList, type)
    }
}