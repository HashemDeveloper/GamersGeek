package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IRawgGameDbApi {
    @Headers("X-RapidAPI-Key: $API_KEY", "User-Agent: $APP_NAME")
    @GET("/games")
    suspend fun fetchAllGames(): Response<GameListRes>
    @Headers("X-RapidAPI-Key: $API_KEY", "User-Agent: $APP_NAME")
    @GET("/games/{id}")
    suspend fun fetchGameById(@Path("id") id: Int): Response<GamesRes>

    companion object {
        private const val API_KEY = "31a50ea584msh31135fa52e8cf43p1e368fjsn03a55f8d0822"
        private const val APP_NAME = "GamersGeek"
    }
}