package com.project.gamersgeek.utils.networkconnections

import android.content.Context
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.gamersgeek.R
import com.project.gamersgeek.broadcast.GamersGeekBroadcastReceiver
import com.project.gamersgeek.utils.Constants
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ConnectionStateMonitor @Inject constructor(private val context: Context) : LiveData<Boolean>(),
    IConnectionStateMonitor, CoroutineScope {

    @Inject
    lateinit var gamersGeekBroadcastReceiver: GamersGeekBroadcastReceiver
    private var networkCallback: ConnectivityManager.NetworkCallback?= null
    private var connectivityManager: ConnectivityManager?= null
    private val wifiConnectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val usingMobileDataLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val connectedNoInternetLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val job = Job()
    init {
        this.connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.networkCallback =
                NearDocNetworkCallback(
                    this, this.connectivityManager!!, this.context
                )
        }
    }

    override fun onActive() {
        super.onActive()
        registerNetworkCallbacks()
    }

    override fun onInactive() {
        super.onInactive()
        unregisterCallbacks()
    }

    private fun registerNetworkCallbacks() {
        launch {
            withContext(Dispatchers.IO) {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                        connectivityManager!!.registerDefaultNetworkCallback(networkCallback!!)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        val networkRequest = NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build()
                        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
                    }
                    else -> {
                        val isWifiConnected: Boolean = connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected
                        val isMobileConnected: Boolean = connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.isConnected
                        if (isWifiConnected || isMobileConnected) {
                            if (PingNetwork.isOnline) {
                                if (isWifiConnected) {
                                    withContext(Dispatchers.Main) {
                                        wifiConnectedLiveData.postValue(true)
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        usingMobileDataLiveData.postValue(true)
                                    }
                                }
                            }
                        }
                        context.registerReceiver(gamersGeekBroadcastReceiver, IntentFilter(Constants.CONNECTIVITY_ACTION))
                    }
                }
            }
        }
    }

    private fun unregisterCallbacks() {
        launch {
            withContext(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    connectivityManager?.unregisterNetworkCallback(networkCallback!!)
                } else {
                    context.unregisterReceiver(gamersGeekBroadcastReceiver)
                }
            }
        }
    }

    override fun isUsingWifiLiveData(): LiveData<Boolean> {
       return this.wifiConnectedLiveData
    }
    override fun isUsingMobileData(): LiveData<Boolean> {
        return this.usingMobileDataLiveData
    }

    override fun isConnectedNoInternetLiveData(): LiveData<Boolean> {
        return this.connectedNoInternetLiveData
    }
    override fun updateConnection() {
        launch {
            withContext(Dispatchers.IO) {
                if (connectivityManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        var network: Network?= null
                        for (networks in connectivityManager!!.allNetworks) {
                            network = networks
                        }
                        if (connectivityManager!!.getNetworkCapabilities(network) != null) {
                            if (connectivityManager!!.getNetworkCapabilities(network)!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, R.string.using_wifi, Toast.LENGTH_SHORT).show()
                                }
                            } else if (connectivityManager!!.getNetworkCapabilities(network)!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, R.string.using_mobile_data, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                postValue(false)
                            }
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            postValue(false)
                        }
                    }
                }
            }
        }
    }
    override fun getObserver(): ConnectionStateMonitor {
        return this
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    class NearDocNetworkCallback(
        private var connectionStateMonitor: ConnectionStateMonitor,
        private var connectivityManager: ConnectivityManager,
        private var context: Context
    ) :
        ConnectivityManager.NetworkCallback() {
        private var isNetCallBackRegistered: Boolean= false
        private var isNetworkAvailable: Boolean = false

        init {
            val netCallBackHandler = Handler()
            netCallBackHandler.postDelayed({
               if (!this.isNetCallBackRegistered) {
                   this.connectionStateMonitor.updateConnection()
               }
            }, 10000)
        }

        override fun onAvailable(network: Network) {
            this.isNetCallBackRegistered = true
            this.isNetworkAvailable = true
            if (this.connectivityManager.getNetworkCapabilities(network) != null) {
                if (this.connectivityManager.getNetworkCapabilities(network)!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Toast.makeText(this.context, R.string.using_wifi, Toast.LENGTH_SHORT).show()
                } else if (this.connectivityManager.getNetworkCapabilities(network)!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Toast.makeText(this.context, R.string.using_mobile_data, Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onLost(network: Network) {
            this.isNetCallBackRegistered = true
            this.connectionStateMonitor.postValue(false)
            Toast.makeText(this.context, "Connection lost", Toast.LENGTH_SHORT).show()
        }

        override fun onUnavailable() {
            this.isNetCallBackRegistered = true
            this.connectionStateMonitor.postValue(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            if (!networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                this.connectionStateMonitor.postValue(false)
            } else {
                if (this.isNetworkAvailable) {
                    this.connectionStateMonitor.postValue(true)
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.Main
}