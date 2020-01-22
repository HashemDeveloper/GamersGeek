package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler

interface IRawgGameDbApiHelper {
    suspend fun fetchAllGames(page: Int, pageSize: Int): ResultHandler<GameListRes>
    suspend fun fetchGameById(id: Int): ResultHandler<GamesRes>
    suspend fun fetchAllGamePlatforms(page: Int, pageSize: Int): ResultHandler<PlatformRes>
    suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformRes>
}