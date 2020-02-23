package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.Rating
import java.lang.reflect.Type

class GameRatingTypeConverter {
    @TypeConverter
    fun stringToRating(json: String): List<Rating> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Rating>>(){}.type
        return gson.fromJson(json, type);
    }
    @TypeConverter
    fun ratingToString(ratings: List<Rating>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Rating>>(){}.type
        return gson.toJson(ratings, type)
    }
}