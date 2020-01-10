package com.project.gamersgeek.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.gamersgeek.data.local.entities.Games

@Database(entities = [Games::class], version = 1, exportSchema = false)
abstract class GamerGeeksLocalDb: RoomDatabase() {




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