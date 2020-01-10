package com.project.gamersgeek.utils

data class ResultHandler<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> onLoading(data: T?= null): ResultHandler<T> {
            return ResultHandler(Status.LOADING, data, null)
        }
        fun <T> onSuccess(data: T): ResultHandler<T> {
            return ResultHandler(Status.SUCCESS, data, null)
        }
        fun <T> onError(message: String, data: T?= null): ResultHandler<T> {
            return ResultHandler(Status.ERROR, data, message)
        }
    }
}