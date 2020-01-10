package com.project.gamersgeek.data.remote

import com.project.gamersgeek.data.BaseDataSource
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
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
}