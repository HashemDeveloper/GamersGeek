package com.project.gamersgeek.di

import android.content.Context
import com.project.gamersgeek.GamersGeekApp
import com.project.gamersgeek.data.GamerGeekRepository
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.*
import com.project.gamersgeek.data.remote.GamersGeekRemoteRepo
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.neardoc.rxeventbus.IRxEventBus
import com.project.neardoc.rxeventbus.RxEventBus
import com.project.gamersgeek.utils.networkconnections.ConnectionStateMonitor
import com.project.gamersgeek.utils.networkconnections.IConnectionStateMonitor
import com.project.gamersgeek.utils.networkconnections.IUpdateNetLowApiHelper
import com.project.gamersgeek.utils.networkconnections.UpdateNetLowApiHelper
import com.project.gamersgeek.utils.search.GamersGeekSearchSuggestion
import com.project.gamersgeek.utils.search.IGamersGeekSearchSuggestion
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun provideApplicationContext(app: GamersGeekApp): Context {
        return app
    }
    @Singleton
    @Provides
    fun provideRawgGameDbApi(gamersGeekRemoteRepo: GamersGeekRemoteRepo): IRawgGamerGeekApiHelper {
        return gamersGeekRemoteRepo
    }
    @Singleton
    @Provides
    fun provideSharedPref(context: Context): SharedPrefService {
        return SharedPrefService.invoke(context)
    }
    @Singleton
    @Provides
    fun provideSharedPrefService(sharedPrefService: SharedPrefService): ISharedPrefService {
        return sharedPrefService
    }
    @Singleton
    @Provides
    fun provideGamersGeekRepository(gamerGeekRepository: GamerGeekRepository): IGamerGeekRepository {
        return gamerGeekRepository
    }
    @Singleton
    @Provides
    fun provideIConnectionStateMonitor(connectionStateMonitor: ConnectionStateMonitor): IConnectionStateMonitor {
        return connectionStateMonitor
    }
    //local db start
    @Singleton
    @Provides
    fun providePlatformDetailsDb(dbService: GamerGeeksLocalDbService): IPlatformDetailsDao {
        return dbService.getPlatformDetailsDao()
    }
    @Singleton
    @Provides
    fun provideGameResultDb(dbService: GamerGeeksLocalDbService) : IGameResultDao {
        return dbService.getGameResultDao()
    }
    @Singleton
    @Provides
    fun provideSavedGameDb(dbService: GamerGeeksLocalDbService): ISavedGamesDao {
        return dbService.getSavedGameDao()
    }
    @Singleton
    @Provides
    fun provideSuggestionDao(dbService: GamerGeeksLocalDbService): ISuggestionsDao {
        return dbService.getSuggestionDao()
    }
    @Singleton
    @Provides
    fun provideLocalDbRepo(saveGameRepo: SaveGameRepo): ISaveGameRepo {
        return saveGameRepo
    }
    //local db end
    @Singleton
    @Provides
    fun provideRxEventBus(rxEventBus: RxEventBus): IRxEventBus {
        return rxEventBus
    }
    @Singleton
    @Provides
    fun provideLowApiNetChangeHelper(updateNetLowApiHelper: UpdateNetLowApiHelper): IUpdateNetLowApiHelper {
        return updateNetLowApiHelper
    }
    @Singleton
    @Provides
    fun provideSearchSuggestion(gamersGeekSearchSuggestion: GamersGeekSearchSuggestion): IGamersGeekSearchSuggestion {
        return gamersGeekSearchSuggestion
    }
    @Singleton
    @Provides
    fun provideGameResultRepo(gameResultRepo: GameResultRepo): IGameResultRepo {
        return gameResultRepo
    }
    @Singleton
    @Provides
    fun provideSearchHistory(suggestionsRep: SuggestionsRepo): ISuggestionRepo {
        return suggestionsRep
    }
}