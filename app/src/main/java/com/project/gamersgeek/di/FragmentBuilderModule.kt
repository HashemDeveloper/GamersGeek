package com.project.gamersgeek.di

import com.project.gamersgeek.views.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeAllGamesPageFragment(): AllGamesPage
    @ContributesAndroidInjector
    abstract fun contributePlatformPageFragment(): PlatformsPage
    @ContributesAndroidInjector
    abstract fun contributePlatformDetailPageFragment(): PlatformDetailsPage
    @ContributesAndroidInjector
    abstract fun contributeSavedGamePageFragment(): SavedGamesPage
    @ContributesAndroidInjector
    abstract fun contributeGameDetailsPageFragment(): GameDetailsPage
    @ContributesAndroidInjector
    abstract fun contributePublisherPageFragment(): PublisherPage
    @ContributesAndroidInjector
    abstract fun contributeDeveloperPage(): DeveloperPage
    @ContributesAndroidInjector
    abstract fun contributeCreatorPage(): CreatorsPage
    @ContributesAndroidInjector
    abstract fun contributeStorePage(): StorePage
}