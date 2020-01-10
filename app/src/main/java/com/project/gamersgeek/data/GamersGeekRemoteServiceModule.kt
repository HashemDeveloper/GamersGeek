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
    internal fun provideGamersGeekRemoteApi(retrofit: Retrofit): IRawgGameDbApi {
        return retrofit.create(IRawgGameDbApi::class.java)
    }
}