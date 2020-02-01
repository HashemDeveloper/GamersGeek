package com.project.gamersgeek.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.project.gamersgeek.utils.Constants
import dagger.android.AndroidInjection
import javax.inject.Inject

class GamersGeekBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    override fun onReceive(contex: Context?, intent: Intent?) {
      AndroidInjection.inject(this, contex)
        when (intent!!.action) {
            Constants.CONNECTIVITY_ACTION -> {

            }
        }
    }
}