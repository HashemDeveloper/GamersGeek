package com.project.gamersgeek.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.typeconverters.PlatformGameTypeConverter

@Database(entities = [PlatformDetails::class], version = 1, exportSchema = false)
@TypeConverters(PlatformGameTypeConverter::class)
abstract class GamerGeeksLocalDb: RoomDatabase() {

    abstract fun getPlatformDetailsDao(): IPlatformDetailsDao

    companion object {
        private const val DB_NAME = "GamerGeeks"
        @Volatile
        private var instance: GamerGeeksLocalDb?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildLocalDb(context).also {
                instance = it
            }
        }
        private fun buildLocalDb(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            GamerGeeksLocalDb::class.java,
            DB_NAME
        ).build()
    }
}