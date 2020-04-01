package com.project.gamersgeek.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.project.gamersgeek.models.platforms.PlatformDetails

@Dao
interface IPlatformDetailsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(platformDetails: List<PlatformDetails>)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(platformDetails: List<PlatformDetails>)
    @Transaction @Query("delete from platform_details")
    suspend fun clearPlatformDetails()
    @Transaction @Query("select * from platform_details")
    fun getAllPlatformDetails(): DataSource.Factory<Int, PlatformDetails>
    @Transaction @Query("select * from platform_details order by id")
    suspend fun getPlatformDetails(): PlatformDetails
}