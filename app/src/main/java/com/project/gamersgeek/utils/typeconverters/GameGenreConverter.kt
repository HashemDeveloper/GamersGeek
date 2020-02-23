package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.GameGenre
import java.lang.reflect.Type

class GameGenreConverter {
    @TypeConverter
    fun stringToGameGenreConverter(json: String): List<GameGenre> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<GameGenre>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun gameGenreToString(gameGenreList: List<GameGenre>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<GameGenre>>(){}.type
        return gson.toJson(gameGenreList, type)
    }
}