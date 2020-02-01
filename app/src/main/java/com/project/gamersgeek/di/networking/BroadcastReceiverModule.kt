package com.project.gamersgeek.di.networking

import com.project.gamersgeek.broadcast.GamersGeekBroadcastReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributeBroadcastReceiver(): GamersGeekBroadcastReceiver
}