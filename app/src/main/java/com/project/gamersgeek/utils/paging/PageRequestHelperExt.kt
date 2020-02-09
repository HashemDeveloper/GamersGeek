package com.project.gamersgeek.utils.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun PagingRequestHelper.createNetworkStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener {
        object : PagingRequestHelper.Listener {
            override fun onStatusChange(report: () -> StatusReport) {
                when {
                    report.invoke().hasRunning() -> liveData.postValue(NetworkState.LOADING)
                    report.invoke().hasError() -> liveData.postValue(NetworkState.error(
                        getErrorMessage(report.invoke())))
                    else -> liveData.postValue(NetworkState.LOADED)
                }
            }
        }
    }
    return liveData
}
private fun getErrorMessage(report: StatusReport): String {
    return RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}