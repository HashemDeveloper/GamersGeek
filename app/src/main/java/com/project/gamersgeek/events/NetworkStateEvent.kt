package com.project.gamersgeek.events

import com.project.gamersgeek.utils.networkconnections.GamersGeekNetworkType

class NetworkStateEvent(private val networkAvailable: Boolean, private val type: GamersGeekNetworkType?) {
    fun getIsNetworkAvailable(): Boolean {
        return this.networkAvailable
    }
    fun getNetworkType(): GamersGeekNetworkType? {
        return this.type
    }
}