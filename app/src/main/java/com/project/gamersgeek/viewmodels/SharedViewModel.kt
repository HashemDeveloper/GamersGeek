package com.project.gamersgeek.viewmodels

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SharedViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    private val job = Job()
    private var drawerLayout: DrawerLayout?= null

    fun setupNavDrawer(drawerLayout: DrawerLayout?) {
        this.drawerLayout = drawerLayout
    }

    fun toggleDrawer() {
        Constants.toggleDrawer(this.drawerLayout)
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}