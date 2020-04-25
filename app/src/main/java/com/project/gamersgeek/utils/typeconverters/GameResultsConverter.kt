package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.Results
import java.lang.reflect.Type

class GameResultsConverter {
    @TypeConverter
    fun stringToResult(json: String?): Results? {
        val gson = Gson()
        val type: Type = object : TypeToken<Results>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun resultToString(results: Results?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<Results>(){}.type
        return gson.toJson(results, type)
    }
}