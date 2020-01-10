package com.project.gamersgeek.data.local

import android.content.SharedPreferences

interface ISharedPrefService {
    fun registerOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
}