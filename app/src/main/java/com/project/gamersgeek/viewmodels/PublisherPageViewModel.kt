package com.project.gamersgeek.viewmodels

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PublisherPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    private var job = Job()
    @Inject
    lateinit var api: IRawgGamerGeekApiHelper
    var publisherLiveData: LiveData<ResultHandler<BaseResModel<DevPubResult>>>?= null

    fun fetchPublisherList() {
        this.publisherLiveData = gamersGeekLiveData {
            this.api.fetchAllPubInfo()
        }
    }

    fun toggleDrawer() {

    }

    fun setupNavDrawer(navigationDrawerLayoutId: DrawerLayout?) {

    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

}