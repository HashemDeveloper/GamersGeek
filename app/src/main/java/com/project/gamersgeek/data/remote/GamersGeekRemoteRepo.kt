package com.project.gamersgeek.data.remote

import com.project.gamersgeek.data.BaseDataSource
import com.project.gamersgeek.data.local.IGameResultDao
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.models.stores.StoreResult
import com.project.gamersgeek.utils.ResultHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GamersGeekRemoteRepo @Inject constructor(): IRawgGamerGeekApiHelper, BaseDataSource() {
    @Inject
    lateinit var rawgGameDbApi: IRawgGameDbApi
    @Inject
    lateinit var iGameResultDao: IGameResultDao

    override suspend fun fetchAllGames(page: Int, pageSize: Int): ResultHandler<GameListRes> {
        return getResult {
            this.rawgGameDbApi.fetchAllGames(page, pageSize, "released")
        }
    }

    override suspend fun fetchGameById(id: Int): ResultHandler<GamesRes> {
       return getResult {
           this.rawgGameDbApi.fetchGameById(id)
       }
    }

    override suspend fun fetchAllGamePlatforms(page: Int, pageSize: Int): ResultHandler<BaseResModel<PlatformDetails>> {
        return getResult {
            this.rawgGameDbApi.getAllListOfVideoGamePlatform(page, pageSize, "id")
        }
    }

    override suspend fun getGamePlatformDetails(id: Int): ResultHandler<PlatformDetails> {
        return getResult {
            this.rawgGameDbApi.getPlatformDetails(id)
        }
    }

    override suspend fun fetchAllDevInfo(): ResultHandler<BaseResModel<DevPubResult>> {
        return getResult {
            this.rawgGameDbApi.getAllDevelopers()
        }
    }

    override suspend fun fetchAllPubInfo(): ResultHandler<BaseResModel<DevPubResult>> {
        return getResult {
            this.rawgGameDbApi.getAllPublishers()
        }
    }

    override fun onSearchAllGames(value: String): GameListRes? {
        var gameListRes: GameListRes?= null
        runBlocking {
            if (searchAllGames(value) != null) {
                val job: Deferred<GameListRes> = async { searchAllGames(value)!! }
                gameListRes = job.await()
                gameListRes?.let { list ->
                    val result: List<Results>?= list.results
                    result?.let {r ->
                        iGameResultDao.insertGameList(r)
                    }
                }
            }
        }
        return gameListRes
    }

    override suspend fun fetchGameCreators(): ResultHandler<BaseResModel<CreatorResults>> {
        return getResult {
            this.rawgGameDbApi.getGameCreators()
        }
    }

    override suspend fun fetchGameStores(): ResultHandler<BaseResModel<StoreResult>> {
        return getResult {
            this.rawgGameDbApi.getGameStores()
        }
    }

    private suspend fun searchAllGames(value: String): GameListRes? {
        return rawgGameDbApi.searchGames(value).body()
    }
}