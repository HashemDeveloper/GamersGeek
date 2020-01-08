package com.project.gamersgeek

import android.app.Application
import com.project.gamersgeek.di.ApplicationInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class GamersGeekApp: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        ApplicationInjector.init(this)
    }


    override fun androidInjector(): AndroidInjector<Any> {
       return this.dispatchingAndroidInjector
    }
}