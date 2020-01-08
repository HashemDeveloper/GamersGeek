package com.project.gamersgeek.di

import com.project.gamersgeek.GamersGeekApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun buildApplication(app: GamersGeekApp): Builder
        fun build(): ApplicationComponent
    }
    fun inject(app: GamersGeekApp)
}