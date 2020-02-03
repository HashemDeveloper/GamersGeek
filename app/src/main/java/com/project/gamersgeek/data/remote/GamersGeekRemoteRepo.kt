package com.project.gamersgeek.data.remote

import com.project.gamersgeek.data.BaseDataSource
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import javax.inject.Inject

class GamersGeekRemoteRepo @Inject constructor(): IRawgGameDbApiHelper, BaseDataSource() {
    @Inject
    lateinit var rawgGameDbApi: IRawgGameDbApi

    override suspend fun fetchAllGames(page: Int, pageSize: Int): ResultHandler<GameListRes> {
        return getResult {
            this.rawgGameDbApi.fetchAllGames(page, pageSize)
        }
    }

    override suspend fun fetchGameById(id: Int): ResultHandler<GamesRes> {
       return getResult {
           this.rawgGameDbApi.fetchGameById(id)
       }
    }

    override suspend fun fetchAllGamePlatforms(page: Int, pageSize: Int): ResultHandler<PlatformRes> {
        return getResult {
            this.rawgGameDbApi.getAllListOfVideoGamePlatform(page, pageSize, "id")
        }
    }

    override suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformRes> {
        return getResult {
            this.rawgGameDbApi.getPlatformDetails(id)
        }
    }
}