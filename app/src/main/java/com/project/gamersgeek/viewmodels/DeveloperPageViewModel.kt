package com.project.gamersgeek.viewmodels

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DeveloperPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    private var job = Job()
    @Inject
    lateinit var gamersGeekRemoteRepo: IRawgGamerGeekApiHelper
    var developerListLiveData: LiveData<ResultHandler<BaseResModel<DevPubResult>>>?= null
    private var drawerLayout: DrawerLayout?= null

    fun getDevelopersList() {
        this.developerListLiveData = gamersGeekLiveData {
            this.gamersGeekRemoteRepo.fetchAllDevInfo()
        }
    }

    fun toggleDrawer() {
        Constants.toggleDrawer(this.drawerLayout)
    }

    fun setupNavDrawer(drawerLayout: DrawerLayout?) {
        this.drawerLayout = drawerLayout
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}