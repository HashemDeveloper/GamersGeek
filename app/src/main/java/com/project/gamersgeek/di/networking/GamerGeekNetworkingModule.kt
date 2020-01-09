package com.project.gamersgeek.di.networking

import android.util.Log
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.GamersGeekRemoteServiceModule
import dagger.Module
import dagger.Provides
import okhttp3.*
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [GamersGeekRemoteServiceModule::class])
object GamerGeekNetworkingModule {
    private const val READ_TIMEOUT_TIME: Long = 1000
    private const val CONNECTION_TIMEOUT_TIME: Long = 1000
    private const val API_KEY = "31a50ea584msh31135fa52e8cf43p1e368fjsn03a55f8d0822"
    @Singleton
    @Provides
    @JvmStatic
    internal fun provideOkHttpClient(): Call.Factory {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_TIME, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_TIME, TimeUnit.MILLISECONDS)
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val request: Request = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Host", "rawg-video-games-database.p.rapidapi.com")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("X-RapidAPI-Key", API_KEY)
                    .method(originalRequest.method(), originalRequest.body())
                    .build()
                val response: Response = chain.proceed(request)
                if (BuildConfig.DEBUG) {
                    Log.d("GamersGeek --->", "Code: " + response.code())
                }
                try {
                    if (response.code() == 401) {
                        return@addInterceptor response
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.d("RetrofitError: ", e.localizedMessage!!)
                    }
                } finally {
                    if (response.body() != null) {
                        response.body()!!.close()
                    }
                }
                response
            }
            .build()
    }
    @Singleton
    @Named("base_url")
    @JvmStatic
    internal fun provideBaseUrl(): String {
        return "https://rawg-video-games-database.p.rapidapi.com/"
    }
}