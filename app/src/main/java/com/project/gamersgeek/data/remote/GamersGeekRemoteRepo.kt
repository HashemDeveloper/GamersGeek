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

    override suspend fun fetchAllGames(): ResultHandler<GameListRes> {
        return getResult {
            this.rawgGameDbApi.fetchAllGames()
        }
    }

    override suspend fun fetchGameById(id: Int): ResultHandler<GamesRes> {
       return getResult {
           this.rawgGameDbApi.fetchGameById(id)
       }
    }

    override suspend fun fetchGamePlatforms(): ResultHandler<PlatformRes> {
        return getResult {
            this.rawgGameDbApi.getAllListOfVideoGamePlatform()
        }
    }

    override suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformRes> {
        return getResult {
            this.rawgGameDbApi.getPlatformDetails(id)
        }
    }
}