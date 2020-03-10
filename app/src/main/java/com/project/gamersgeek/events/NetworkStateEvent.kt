package com.project.gamersgeek.events

import com.project.gamersgeek.utils.networkconnections.GamersGeekNetworkType

class NetworkStateEvent(private val networkAvailable: Boolean) {
    fun getIsNetworkAvailable(): Boolean {
        return this.networkAvailable
    }
}