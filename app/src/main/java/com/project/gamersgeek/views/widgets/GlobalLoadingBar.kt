package com.project.gamersgeek.views.widgets

import android.view.View
import androidx.fragment.app.FragmentActivity

class GlobalLoadingBar constructor(private val container: View, private val activity: FragmentActivity): BaseGlobalLoadingBar() {
    fun startLoading(isLoading: Boolean) {
        startLoading(this.container, this.activity, isLoading)
    }
}