package com.project.gamersgeek.data.local

import android.content.Context
import android.widget.Toast
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.SaveGames
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SaveGameRepo @Inject constructor(private val context: Context): ISaveGameRepo, CoroutineScope {
    @Inject
    lateinit var savedGamesDao: ISavedGamesDao
    private val job = Job()



    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    override fun saveGames(saveGames: SaveGames) {
       launch {
           val insert: Flow<Long>? = insertData(saveGames)
           insert?.collect { value ->
               val id: Long?= value
               if (id != null) {
                   withContext(Dispatchers.Main) {
                       Toast.makeText(context, context.getString(R.string.game_saved), Toast.LENGTH_SHORT).show()
                   }
               }
           }
       }
    }
    private fun insertData(saveGames: SaveGames): Flow<Long> = flow {
        val value: Long = savedGamesDao.insert(saveGames)
        emit(value)
    }

    override fun getPlayedGames(): List<SaveGames>? {
        var savedGameList: List<SaveGames>?= null
        runBlocking {
            if (allPlayedGames() != null) {
                val job: Deferred<List<SaveGames>> = async { allPlayedGames()!! }
                savedGameList = job.await()
            }
        }
        return savedGameList
    }

    override fun getWishedToPlayGames(): List<SaveGames>? {
        var wishToPlayList: List<SaveGames>?= null
        runBlocking {
            if (allWishedToPlayGames() != null) {
                val job: Deferred<List<SaveGames>> = async { allWishedToPlayGames()!! }
                wishToPlayList = job.await()
            }
        }
        return wishToPlayList
    }

    override fun getGameById(id: Int): SaveGames? {
        var savedGame: SaveGames?= null
        runBlocking {
            if (gameById(id) != null) {
                val job: Deferred<SaveGames> = async { gameById(id)!! }
                savedGame = job.await()
            }
        }
        return savedGame
    }

    override fun deleteGamesById(id: Int) {
        launch {
            savedGamesDao.deleteSavedGameById(id)
        }
    }

    override fun deleteAllGames() {
        launch {
            savedGamesDao.deleteAllSavedGames()
        }
    }

    private suspend fun allPlayedGames(): List<SaveGames>? {
        return this.savedGamesDao.getPlayedGames()
    }
    private suspend fun allWishedToPlayGames(): List<SaveGames>? {
        return this.savedGamesDao.getWishedToPlayGames()
    }
    private suspend fun gameById(id: Int): SaveGames? {
        return this.savedGamesDao.getSavedGameById(id)
    }
}