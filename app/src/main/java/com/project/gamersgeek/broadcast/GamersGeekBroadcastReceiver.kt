package com.project.gamersgeek.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.project.gamersgeek.R
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.networkconnections.GamersGeekNetworkType
import com.project.gamersgeek.utils.networkconnections.IUpdateNetLowApiHelper
import com.project.gamersgeek.utils.networkconnections.PingNetwork
import dagger.android.AndroidInjection
import javax.inject.Inject

class GamersGeekBroadcastReceiver @Inject constructor(): BroadcastReceiver() {
    @Inject
    lateinit var iUpdateNetLowApiHelper: IUpdateNetLowApiHelper

    override fun onReceive(contex: Context?, intent: Intent?) {
      AndroidInjection.inject(this, contex)
        when (intent!!.action) {
            Constants.CONNECTIVITY_ACTION -> {
                this.iUpdateNetLowApiHelper.updateNetwork()
            }
        }
    }
}