package com.project.gamersgeek.utils.networkconnections

import com.project.gamersgeek.BuildConfig
import timber.log.Timber
import java.io.IOException

object PingNetwork {
    val isOnline: Boolean
        get() {
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                debug(e)
            } catch (e: InterruptedException) {
                debug(e)
            }
            return false
        }

    private fun <T> debug(e: T?) {
        if (BuildConfig.DEBUG) {
            if (e != null) {
                if (e is IOException) {
                    if ((e as IOException).localizedMessage != null) {
                        Timber.e("IOExceptoin: ${e.message}")
                    }
                } else if (e is InterruptedException) {
                    if ((e as InterruptedException).localizedMessage != null) {
                       Timber.d("InterruptedException: ${e.message}")
                    }
                }
            }
        }
    }
}
