package com.project.gamersgeek.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.project.gamersgeek.models.games.Results
import retrofit2.Response

suspend fun<T> fetchAndSaveData(call: suspend() -> Response<T>,
                                onSuccess: (data: T) -> Unit,
                                onError: (error: String) -> Unit) {
    try {
        val response: Response<T> = call()
        if (response.isSuccessful) {
            val body: T? = response.body()
            body?.let {
                onSuccess(body)
            }
        } else {
            onError(response.message())
        }
    } catch (ex: Exception) {
        if (ex.localizedMessage != null) {
            onError(ex.localizedMessage!!)
        }
    }
}