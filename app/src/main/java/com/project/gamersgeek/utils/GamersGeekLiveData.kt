package com.project.gamersgeek.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

fun <T> gamersGeekLiveData(networkCall: suspend() -> ResultHandler<T>): LiveData<ResultHandler<T>> =
    liveData(Dispatchers.IO) {
        emit(ResultHandler.onLoading<T>())
        val responseStatus: ResultHandler<T> = networkCall.invoke()
        if (responseStatus.status == ResultHandler.Status.SUCCESS) {
            emit(ResultHandler.onSuccess<T>(responseStatus.data!!))
        } else if (responseStatus.status == ResultHandler.Status.ERROR) {
            emit(ResultHandler.onError<T>(responseStatus.message!!))
        }
    }