package com.project.gamersgeek.data.local

import androidx.room.*
import com.project.gamersgeek.utils.search.GameResultWrapper

@Dao
interface ISuggestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchResult: GameResultWrapper): Long
    @Transaction @Query("select * from suggestion_history order by date desc limit 5")
    suspend fun getSuggestions(): List<GameResultWrapper>
    @Transaction @Query("delete from suggestion_history")
    suspend fun deleteSuggestions(): Int
    @Transaction @Query("delete from suggestion_history where suggestion= :name")
    suspend fun deleteHistoryByName(name: String): Int
    @Delete()
    suspend fun deleteOldHistory(suggestionList: List<GameResultWrapper>?): Int
}