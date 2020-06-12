package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.models.stores.StoreResult
import com.project.gamersgeek.utils.ResultHandler

interface IRawgGamerGeekApiHelper {
    suspend fun fetchAllGames(page: Int, pageSize: Int): ResultHandler<GameListRes>
    suspend fun fetchGameById(id: Int): ResultHandler<GamesRes>
    suspend fun fetchAllGamePlatforms(page: Int, pageSize: Int): ResultHandler<BaseResModel<PlatformDetails>>
    suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformDetails>
    fun onSearchAllGames(value: String): GameListRes?
    suspend fun fetchAllDevInfo(): ResultHandler<BaseResModel<DevPubResult>>
    suspend fun fetchAllPubInfo(): ResultHandler<BaseResModel<DevPubResult>>
    suspend fun fetchGameCreators(): ResultHandler<BaseResModel<CreatorResults>>
    suspend fun fetchGameStores(): ResultHandler<BaseResModel<StoreResult>>
}