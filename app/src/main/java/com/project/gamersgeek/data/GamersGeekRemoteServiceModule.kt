package com.project.gamersgeek.data

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object GamersGeekRemoteServiceModule {
    @Singleton
    @Provides
    @JvmStatic
    internal fun provideGamersGeekRemoteApi(retrofit: Retrofit): IGamersGeekApi {
        return retrofit.create(IGamersGeekApi::class.java)
    }
}