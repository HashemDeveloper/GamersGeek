package com.project.gamersgeek.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.gamersgeek.di.scopes.ViewModelKey
import com.project.gamersgeek.viewmodels.AllGamesPageViewModel
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
    abstract fun provideWelcomePageViewModel(allGamesPageViewModel: AllGamesPageViewModel): ViewModel
}