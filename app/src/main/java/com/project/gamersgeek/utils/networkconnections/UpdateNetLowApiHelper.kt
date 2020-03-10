package com.project.gamersgeek.utils.networkconnections

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.project.gamersgeek.R
import javax.inject.Inject

class UpdateNetLowApiHelper @Inject constructor(private val context: Context): IUpdateNetLowApiHelper {
    private var connectivityManager: ConnectivityManager?= null

    init {
        this.connectivityManager = this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    override fun updateNetwork() {
        updateNetworkChange()
    }

    private fun updateNetworkChange() {
        when (isNetworkConnected()) {
            GamersGeekNetworkType.WIFI_DATA -> {
                Toast.makeText(this.context, R.string.using_wifi, Toast.LENGTH_SHORT).show()
            }
            GamersGeekNetworkType.MOBILE_DATA -> {
                Toast.makeText(this.context, R.string.using_mobile_data, Toast.LENGTH_SHORT).show()
            }
            GamersGeekNetworkType.UNAUTHORIZED_INTERNET -> {
                Toast.makeText(this.context, R.string.unauthorized_internet_connection, Toast.LENGTH_SHORT).show()
            }
            GamersGeekNetworkType.NO_NETWORK -> {
                Toast.makeText(this.context, R.string.not_connected_internet, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isNetworkConnected(): GamersGeekNetworkType? {
        var gamersGeekNetworkType: GamersGeekNetworkType?= null
        this.connectivityManager?.let {connectionManager ->
            val isWifiConnected: Boolean = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected
            val isMobileNetConnected: Boolean = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.isConnected
            if (isWifiConnected || isMobileNetConnected) {
                if (PingNetwork.isOnline) {
                    gamersGeekNetworkType = when {
                        isWifiConnected -> {
                            GamersGeekNetworkType.WIFI_DATA
                        }
                        isMobileNetConnected -> {
                            GamersGeekNetworkType.MOBILE_DATA
                        }
                        else -> {
                            GamersGeekNetworkType.UNAUTHORIZED_INTERNET
                        }
                    }
                }
            } else {
                gamersGeekNetworkType = GamersGeekNetworkType.NO_NETWORK
            }
        }
        return if (gamersGeekNetworkType != null) {
            gamersGeekNetworkType
        } else {
            null
        }
    }
}