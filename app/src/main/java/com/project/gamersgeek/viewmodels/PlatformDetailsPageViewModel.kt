package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlatformDetailsPageViewModel @Inject constructor(): ViewModel(), CoroutineScope{
    private val job = Job()
    @Inject
    lateinit var iRawgGamerGeekApiHelper: IRawgGamerGeekApiHelper
    var platformDetailLiveData: LiveData<ResultHandler<PlatformDetails>>? = MutableLiveData()

    fun loadPlatformDetails(id: Int) {
        this.platformDetailLiveData = gamersGeekLiveData {
            this.iRawgGamerGeekApiHelper.getGamePlatformDetails(id)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}