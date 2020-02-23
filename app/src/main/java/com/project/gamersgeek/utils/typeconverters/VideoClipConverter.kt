package com.project.gamersgeek.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.gamersgeek.models.games.VideoClip
import java.lang.reflect.Type

class VideoClipConverter {
    @TypeConverter
    fun stringToVideo(json: String): VideoClip {
        val gson = Gson()
        val type: Type = object : TypeToken<VideoClip>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun videoClipToString(videoClip: VideoClip): String {
        val gson = Gson()
        val type: Type = object : TypeToken<VideoClip>(){}.type
        return gson.toJson(videoClip, type)
    }
}