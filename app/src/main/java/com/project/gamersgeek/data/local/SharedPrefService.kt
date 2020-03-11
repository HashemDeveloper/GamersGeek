package com.project.gamersgeek.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.project.gamersgeek.utils.Constants
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

    override fun setIsNightModeOn(isNightModeOn: Boolean) {
       pref?.edit(commit = true) {
           putBoolean(Constants.IS_NIGHT_MODE, isNightModeOn)
       }
    }

    override fun getIsNightModeOn(): Boolean {
        return pref?.getBoolean(Constants.IS_NIGHT_MODE, false)!!
    }
}