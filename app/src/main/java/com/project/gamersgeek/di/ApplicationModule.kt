package com.project.gamersgeek.di

import android.content.Context
import com.project.gamersgeek.GamersGeekApp
import com.project.gamersgeek.data.GamerGeekRepository
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.local.GamerGeeksLocalDbService
import com.project.gamersgeek.data.local.IPlatformDetailsDao
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.data.local.SharedPrefService
import com.project.gamersgeek.data.remote.GamersGeekRemoteRepo
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.neardoc.rxeventbus.IRxEventBus
import com.project.neardoc.rxeventbus.RxEventBus
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
    fun provideRawgGameDbApi(gamersGeekRemoteRepo: GamersGeekRemoteRepo): IRawgGameDbApiHelper {
        return gamersGeekRemoteRepo
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
    //local db start
    @Singleton
    @Provides
    fun providePlatformDetailsDb(dbService: GamerGeeksLocalDbService): IPlatformDetailsDao {
        return dbService.getPlatformDetailsDao()
    }
    //local db end
    @Singleton
    @Provides
    fun provideRxEventBus(rxEventBus: RxEventBus): IRxEventBus {
        return rxEventBus
    }
}