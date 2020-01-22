package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.IGamerGeekRepository
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import javax.inject.Inject

class PlatformPageViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var iGamerGeekRepository: IGamerGeekRepository

//    var fetchGamePlatforms: LiveData<ResultHandler<PlatformRes?>>? = null
    val fetchGamePlatforms by lazy {
        this.iGamerGeekRepository.getPlatformDetailsPagedData()
    }

    fun fetchGamePlatforms() {
//        this.fetchGamePlatforms = this.iGamerGeekRepository.getAllGamePlatforms()

    }
}