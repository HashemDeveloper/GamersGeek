package com.project.gamersgeek.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class SharedPrefService @Inject constructor(): ISharedPrefService {

    override fun registerOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        listener = sharedPrefListener
    }

    override fun unregisterOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        listener = sharedPrefListener
        if (listener != null) {
            listener = null
        }
    }


    companion object {
        private var pref: SharedPreferences?= null
        @Volatile
        private var instance: SharedPrefService?= null
        private var listener: SharedPreferences.OnSharedPreferenceChangeListener? =null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPrefService = instance ?: synchronized(LOCK) {
            this.instance ?: buildSharedPrefService(context).also {
                instance = it
            }
        }

        private fun buildSharedPrefService(context: Context): SharedPrefService {
            this.pref = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPrefService()
        }
    }
}