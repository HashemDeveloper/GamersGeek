package com.project.gamersgeek.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.games.SaveGames
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.search.GameResultWrapper
import com.project.gamersgeek.utils.typeconverters.*

@Database(entities = [PlatformDetails::class, Results::class, SaveGames::class, GameResultWrapper::class], version = 1, exportSchema = false)
@TypeConverters(PlatformGameTypeConverter::class, GameRatingTypeConverter::class,
    AddedByStatusTypeConverter::class, CategorizedGamePlatformsConverter::class, GameGenericPlatformConverter::class,
    GameGenreConverter::class, StoreConverter::class, VideoClipConverter::class, ShortScreenShotConverter::class)
abstract class GamerGeeksLocalDb: RoomDatabase() {

    abstract fun getPlatformDetailsDao(): IPlatformDetailsDao
    abstract fun getGameResultDao(): IGameResultDao
    abstract fun getSavedGamesDao(): ISavedGamesDao
    abstract fun getSuggestionDao(): ISuggestionsDao

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