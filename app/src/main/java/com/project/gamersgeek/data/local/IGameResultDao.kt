package com.project.gamersgeek.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.project.gamersgeek.models.games.Results

@Dao
interface IGameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameList(gameListRes: List<Results>)
    @Transaction @Query("delete from game_results")
    suspend fun clearAllGameResults()
    @Query("select * from game_results")
    fun getAllGames() : DataSource.Factory<Int, Results>
}