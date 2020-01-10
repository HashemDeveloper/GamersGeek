package com.project.gamersgeek.data

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import retrofit2.http.GET
import retrofit2.http.Path

interface IGamersGeekApi {
    @GET("/games")
    suspend fun fetchAllGames(): GameListRes
    @GET("/games/{id}")
    suspend fun fetchGameById(@Path("id") id: Int): GamesRes
}