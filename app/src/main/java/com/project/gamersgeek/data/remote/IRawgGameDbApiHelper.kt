package com.project.gamersgeek.data.remote

import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.utils.ResultHandler

interface IRawgGameDbApiHelper {
    suspend fun fetchAllGames(): ResultHandler<GameListRes>
    suspend fun fetchGameById(id: Int): ResultHandler<GamesRes>
}