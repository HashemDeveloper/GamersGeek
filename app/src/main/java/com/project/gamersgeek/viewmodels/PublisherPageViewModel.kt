package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.publishers.DevPublisherInfoResponse
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
    var publisherLiveData: LiveData<ResultHandler<DevPublisherInfoResponse>>?= null

    fun fetchPublisherList() {
        this.publisherLiveData = gamersGeekLiveData {
            this.api.fetchAllPubInfo()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

}