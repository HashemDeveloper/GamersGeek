package com.project.gamersgeek.data.local

import androidx.room.*
import com.project.gamersgeek.utils.search.SearchResultWrapper

@Dao
interface ISuggestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchResult: SearchResultWrapper): Long
    @Transaction @Query("select * from suggestion_history where searchFor =:searchFor order by date desc limit 5")
    suspend fun getSuggestions(searchFor: String): List<SearchResultWrapper>
    @Transaction @Query("delete from suggestion_history")
    suspend fun deleteSuggestions(): Int
    @Transaction @Query("delete from suggestion_history where suggestion= :name")
    suspend fun deleteHistoryByName(name: String): Int
    @Delete()
    suspend fun deleteOldHistory(suggestionList: List<SearchResultWrapper>?): Int
    @Transaction @Query("delete from suggestion_history where time_ms < :expiredTime")
    suspend fun deleteOldRecords(expiredTime: Long): Int
}