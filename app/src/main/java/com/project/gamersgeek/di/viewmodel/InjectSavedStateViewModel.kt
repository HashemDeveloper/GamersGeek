package com.project.gamersgeek.di.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.Reusable
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider

@Deprecated("")
@Suppress("UNCHECKED_CAST")
@Reusable
class InjectSavedStateViewModel @Inject constructor(
    private val assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards IAssistedSavedStateViewModelFactory<out ViewModel>>,
    private val viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) {
    fun create(owner: SavedStateRegistryOwner, defaultArg: Bundle?= null) =
        object : AbstractSavedStateViewModelFactory(owner, defaultArg) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
               val viewModel: ViewModel = createAssitedInjectViewModel(modelClass, handle) ?:
               createInjectViewModel(modelClass) ?: throw IllegalArgumentException("Unknown model class $modelClass")

                try {
                    return viewModel as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }

    private fun <T: ViewModel?> createInjectViewModel(modelClass: Class<T>): ViewModel? {
        val creator: Provider<ViewModel> = this.viewModelProviders[modelClass] ?: this.viewModelProviders.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: return null
        return creator.get()
    }

    private fun <T: ViewModel?> createAssitedInjectViewModel(
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): ViewModel? {
       val creator: IAssistedSavedStateViewModelFactory<out ViewModel> =
           this.assistedFactories[modelClass] ?: this.assistedFactories.asIterable().firstOrNull {
           modelClass.isAssignableFrom(it.key)
       }?.value ?: return null
        return creator.create(handle)
    }

}