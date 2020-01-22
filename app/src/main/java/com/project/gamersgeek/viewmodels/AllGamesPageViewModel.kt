package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGameDbApiHelper
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import javax.inject.Inject

class AllGamesPageViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var iRawgGameDbApiHelper: IRawgGameDbApiHelper
    var fetchAllGamesLiveData: LiveData<ResultHandler<GameListRes?>>?= null

    fun fetchAllGames() {
//        this.fetchAllGamesLiveData = gamersGeekLiveData {
//            this.iRawgGameDbApiHelper.fetchAllGames()
//        }
    }
}