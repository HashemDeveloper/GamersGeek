package com.project.gamersgeek.di

import android.content.Context
import com.project.gamersgeek.GamersGeekApp
import com.project.gamersgeek.data.remote.GamersGeekRemoteRepo
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
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
}