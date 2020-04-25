package com.project.gamersgeek.data.local

import androidx.room.*
import com.project.gamersgeek.models.games.SaveGames

@Dao
interface ISavedGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(saveGames: SaveGames): Long
    @Transaction @Query("select * from saved_games where id= :id")
    suspend fun getSavedGameById(id: Int): SaveGames
    @Transaction @Query("delete from saved_games")
    suspend fun deleteAllSavedGames()
    @Transaction @Query("delete from saved_games where id= :id")
    suspend fun deleteSavedGameById(id: Int)
    @Transaction @Query("select * from saved_games where played = 1 order by date")
    suspend fun getPlayedGames(): List<SaveGames>
    @Transaction @Query("select * from saved_games where played = 0 order by date")
    suspend fun getWishedToPlayGames(): List<SaveGames>
}