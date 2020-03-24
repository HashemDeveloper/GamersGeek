package com.project.gamersgeek.data.local

import com.project.gamersgeek.models.games.Results
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GameResultRepo @Inject constructor() : IGameResultRepo, CoroutineScope {
    private val job = Job()
    @Inject
    lateinit var gameResultDao: IGameResultDao

    override fun getAllGameResult(): List<Results>? {
        return getAllGameResultAsync()
    }

    private fun getAllGameResultAsync(): List<Results>? {
        var list: List<Results>? = null
        runBlocking {
            val job: Deferred<List<Results>> = async { getGameResults() }
            list = job.await()
        }
        return list
    }

    private suspend fun getGameResults(): List<Results> {
        return this.gameResultDao.getAllGameResult()
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}