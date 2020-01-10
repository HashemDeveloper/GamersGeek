package com.project.gamersgeek.data

import com.project.gamersgeek.models.games.GameListRes
import retrofit2.http.GET
import retrofit2.http.Path

interface IGamersGeekApi {
    @GET("/games")
    suspend fun fetchAllGames(): GameListRes
}