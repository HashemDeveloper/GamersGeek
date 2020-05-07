package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.ShortScreenShot
import java.lang.reflect.Type

class ShortScreenShotConverter {
    @TypeConverter
    fun stringToShortScreenShot(json: String?): List<ShortScreenShot>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ShortScreenShot>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun shortScreenShotToString(shortScreenShotList: List<ShortScreenShot>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ShortScreenShot>>(){}.type
        return gson.toJson(shortScreenShotList, type)
    }
}