package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.models.publishers.DevPublisherInfoResponse
import com.project.gamersgeek.utils.ResultHandler

interface IRawgGamerGeekApiHelper {
    suspend fun fetchAllGames(page: Int, pageSize: Int): ResultHandler<GameListRes>
    suspend fun fetchGameById(id: Int): ResultHandler<GamesRes>
    suspend fun fetchAllGamePlatforms(page: Int, pageSize: Int): ResultHandler<PlatformRes>
    suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformDetails>
    fun onSearchAllGames(value: String): GameListRes?
    suspend fun fetchAllDevInfo(): ResultHandler<DevPublisherInfoResponse>
    suspend fun fetchAllPubInfo(): ResultHandler<DevPublisherInfoResponse>
}