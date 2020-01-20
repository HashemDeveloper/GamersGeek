package com.project.gamersgeek.di

import com.project.gamersgeek.di.viewmodel.ViewModelModule
import com.project.gamersgeek.views.WelcomePage
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeWelcomePageFragment(): WelcomePage
}