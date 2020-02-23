package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.Store
import java.lang.reflect.Type

class StoreConverter {
    @TypeConverter
    fun stringToStore(json: String): List<Store> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Store>>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun storeToString(storeList: List<Store>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Store>>(){}.type
        return gson.toJson(storeList, type)
    }
}