package com.project.gamersgeek

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.project.gamersgeek.di.ApplicationInjector
import com.project.gamersgeek.utils.theme.ThemeHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class GamersGeekApp: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
        ApplicationInjector.init(this)
        ThemeHelper.applyTheme(ThemeHelper.DEFAULT_MODE)
        AndroidThreeTen.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
       return this.dispatchingAndroidInjector
    }
}