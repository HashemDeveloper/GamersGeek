package com.project.gamersgeek.di

import com.project.gamersgeek.GamersGeekMainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun contributeMainActivity(): GamersGeekMainActivity
}