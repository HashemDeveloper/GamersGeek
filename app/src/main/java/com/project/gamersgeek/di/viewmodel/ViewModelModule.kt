package com.project.gamersgeek.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.gamersgeek.di.scopes.ViewModelKey
import com.project.gamersgeek.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(AllGamesPageViewModel::class)
    internal abstract fun provideWelcomePageViewModel(allGamesPageViewModel: AllGamesPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(PlatformPageViewModel::class)
    internal abstract fun providePlatformPageViewModel(platformPageViewModel: PlatformPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(PlatformDetailsPageViewModel::class)
    internal abstract fun providePlatformDetailsPageViewModel(platformDetailsPageViewModel: PlatformDetailsPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(GameDetailsPageViewModel::class)
    internal abstract fun provideGameDetailPageViewModel(gameDetailsPageViewModel: GameDetailsPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(SavedGamesViewModel::class)
    internal abstract fun provideSavedGameViewModel(savedGamesViewModel: SavedGamesViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(DeveloperPageViewModel::class)
    internal abstract fun provideDeveloperPageViewModel(developerPageViewModel: DeveloperPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(PublisherPageViewModel::class)
    internal abstract fun providePublihserPageViewModel(publihserPageViewModel: PublisherPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(CreatorPageViewModel::class)
    internal abstract fun provideCreatorPageViewModel(creatorPageViewModel: CreatorPageViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    internal abstract fun provideSharedViewModel(sharedViewModel: SharedViewModel): ViewModel
}