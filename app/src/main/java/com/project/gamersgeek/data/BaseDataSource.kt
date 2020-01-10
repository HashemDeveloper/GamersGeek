package com.project.gamersgeek.data

import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.utils.ResultHandler
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend() -> Response<T>): ResultHandler<T> {
        return try {
            val response: Response<T> = call()
            if (response.isSuccessful) {
                val body: T? = response.body()
                if (body != null) {
                    ResultHandler.onSuccess(body)
                } else {
                    ResultHandler.onError("ResponseBody Was Empty")
                }
            } else {
                error("${response.code()}: ${response.message()}")
            }
        } catch (ex: Exception) {
            error(ex.message ?: ex.toString())
        }
    }
    private fun <T> error(message: String?): ResultHandler<T> {
        if (BuildConfig.DEBUG) {
            Timber.e(message)
        }
        return ResultHandler.onError("Call failed for following reason: $message")
    }
}