package com.project.gamersgeek.di

import com.project.gamersgeek.GamersGeekApp
import com.project.gamersgeek.di.networking.GamerGeekNetworkingModule
import com.project.gamersgeek.di.networking.RetrofitModule
import com.project.gamersgeek.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityModule::class,
    RetrofitModule::class,
    ViewModelModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun buildApplication(app: GamersGeekApp): Builder
        fun build(): ApplicationComponent
    }
    fun inject(app: GamersGeekApp)
}