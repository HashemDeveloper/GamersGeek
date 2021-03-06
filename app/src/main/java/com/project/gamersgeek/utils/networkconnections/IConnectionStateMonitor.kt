package com.project.gamersgeek.utils.networkconnections

import androidx.lifecycle.LiveData
import com.project.gamersgeek.utils.networkconnections.ConnectionStateMonitor

interface IConnectionStateMonitor {
    fun isUsingWifiLiveData(): LiveData<Boolean>
    fun isUsingMobileData(): LiveData<Boolean>
    fun isConnectedNoInternetLiveData(): LiveData<Boolean>
    fun updateConnection()
    fun getObserver(): ConnectionStateMonitor
}