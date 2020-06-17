package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.models.stores.GameStoreToBuy
import com.project.gamersgeek.models.stores.StoreResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface IRawgGameDbApi {
    @Headers("Content-Type: application/json","User-Agent: $APP_NAME")
    @GET("api/games")
    suspend fun fetchAllGames(@Query("page") page: Int, @Query("page_size") pageSize: Int, @Query("ordering") ordering: String): Response<GameListRes>
    @Headers("Content-Type: application/json","User-Agent: $APP_NAME")
    @GET("api/games/{id}")
    suspend fun fetchGameById(@Path("id") id: Int): Response<GamesRes>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/platforms")
    suspend fun getAllListOfVideoGamePlatform(@Query("page") page: Int, @Query("page_size") pageSize: Int, @Query("ordering") ordering: String): Response<BaseResModel<PlatformDetails>>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/platforms/{id}")
    suspend fun getPlatformDetails(@Path("id") id: Int): Response<PlatformDetails>
    @Headers("Content-Type: application/json,", "User-Agent: $APP_NAME")
    @GET("api/games/{game_slug}/stores")
    suspend fun getListOfWhereToBuy(@Path("game_slug") gameSlug: String): Response<GameStoreToBuy>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/games")
    suspend fun searchGames(@Query("search") value: String): Response<GameListRes>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/publishers")
    suspend fun getAllPublishers(): Response<BaseResModel<DevPubResult>>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/developers")
    suspend fun getAllDevelopers(): Response<BaseResModel<DevPubResult>>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/creators")
    suspend fun getGameCreators(): Response<BaseResModel<CreatorResults>>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("api/stores")
    suspend fun getGameStores(): Response<BaseResModel<StoreResult>>
    companion object {
        private const val APP_NAME = "GamersGeek"
    }
}