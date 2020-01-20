package com.project.gamersgeek.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface IAssistedSavedStateViewModelFactory<T: ViewModel> {
    fun create(saveStateHandle: SavedStateHandle): T
}