package com.project.gamersgeek.di

import com.project.gamersgeek.views.WelcomePage
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeWelcomePageFragment(): WelcomePage
}