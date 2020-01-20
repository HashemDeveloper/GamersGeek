package com.project.gamersgeek.di.networking

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.remote.GamersGeekRemoteServiceModule
import dagger.Module
import dagger.Provides
import okhttp3.*
import timber.log.Timber
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
    private const val APP_NAME = "GamersGeek"
    @Singleton
    @Provides
    @JvmStatic
    internal fun provideOkHttpClient(): Call.Factory {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_TIME, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_TIME, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val request: Request = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Host", "api.rawg.io")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .method(originalRequest.method(), originalRequest.body())
                    .build()
                val response: Response = chain.proceed(request)
                if (BuildConfig.DEBUG) {
                    Timber.tag("GamersGeek --->").d("Code: %s", response.code())
                }
                try {
                    if (response.code() == 401) {
                        return@addInterceptor response
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Timber.tag("RetrofitError: ").d(e.localizedMessage!!)
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

    @Provides
    @Named("base_url")
    @JvmStatic
    internal fun provideBaseUrl(): String {
        return "https://api.rawg.io/"
    }
}