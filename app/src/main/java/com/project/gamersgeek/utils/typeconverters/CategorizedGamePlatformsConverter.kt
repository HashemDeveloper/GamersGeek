package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import java.lang.reflect.Type

class CategorizedGamePlatformsConverter {
    @TypeConverter
    fun stringToCategorizedGamePlatforms(json: String): List<CategorizedGamePlatforms> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<CategorizedGamePlatforms>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun categorizedGamePlatformsToString(cateGamePlatList: List<CategorizedGamePlatforms>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<CategorizedGamePlatforms>>(){}.type
        return gson.toJson(cateGamePlatList, type)
    }
}