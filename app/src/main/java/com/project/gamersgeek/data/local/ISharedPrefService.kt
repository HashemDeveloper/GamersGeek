package com.project.gamersgeek.data.local

import android.content.SharedPreferences

interface ISharedPrefService {
    fun registerOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun setIsNightModeOn(isNightModeOn: Boolean)
    fun getIsNightModeOn(): Boolean
    fun getSavedGamePlatformImage(): String
    fun storeGameProfileHeader(json: String)
}