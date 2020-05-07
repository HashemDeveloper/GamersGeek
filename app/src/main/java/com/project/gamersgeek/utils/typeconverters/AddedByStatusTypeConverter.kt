package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.AddedByStatus
import java.lang.reflect.Type

class AddedByStatusTypeConverter {
    @TypeConverter
    fun stringToAddedByStatus(json: String?): AddedByStatus? {
        val gson = Gson()
        val type: Type = object : TypeToken<AddedByStatus>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun addedByStatusToString(addedByStatus: AddedByStatus?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<AddedByStatus>(){}.type
        return gson.toJson(addedByStatus, type)
    }
}