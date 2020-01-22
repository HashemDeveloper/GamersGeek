package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface IRawgGameDbApi {
    @Headers("Content-Type: application/json","User-Agent: $APP_NAME")
    @GET("api/games")
    suspend fun fetchAllGames(@Query("page") page: Int, @Query("page_size") pageSize: Int): Response<GameListRes>
    @Headers("Content-Type: application/json","User-Agent: $APP_NAME")
    @GET("api/games/{id}")
    suspend fun fetchGameById(@Path("id") id: Int): Response<GamesRes>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/platforms")
    suspend fun getAllListOfVideoGamePlatform(@Query("page") page: Int, @Query("page_size") pageSize: Int): Response<PlatformRes>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/platforms/{id}")
    suspend fun getPlatformDetails(@Path("id") id: Int): Response<PlatformRes>

    companion object {
        private const val APP_NAME = "GamersGeek"
    }
}