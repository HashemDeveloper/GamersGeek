package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.publishers.PublisherGame
import java.lang.reflect.Type

class PublisherGameConverter {
    @TypeConverter
    fun stringToPublisherGame(json: String): PublisherGame? {
        val gson = Gson()
        val type: Type = object : TypeToken<PublisherGame>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun publisherGameToString(publisherGame: PublisherGame?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<PublisherGame>(){}.type
        return gson.toJson(publisherGame, type)
    }
}