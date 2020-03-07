package com.project.gamersgeek.di

import com.project.gamersgeek.di.viewmodel.ViewModelModule
import com.project.gamersgeek.views.AllGamesPage
import com.project.gamersgeek.views.GameDetailsPage
import com.project.gamersgeek.views.PlatformsPage
import com.project.gamersgeek.views.SavedGamesPage
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeAllGamesPageFragment(): AllGamesPage
    @ContributesAndroidInjector
    abstract fun contributePlatformPageFragment(): PlatformsPage
    @ContributesAndroidInjector
    abstract fun contributeSavedGamePageFragment(): SavedGamesPage
    @ContributesAndroidInjector
    abstract fun contributeGameDetailsPageFragment(): GameDetailsPage
}