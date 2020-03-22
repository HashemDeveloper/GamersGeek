package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.local.ISaveGameRepo
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.games.SaveGames
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GameDetailsPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var gamerGeekRepository: IRawgGamerGeekApiHelper
    @Inject
    lateinit var iSaveGameRepo: ISaveGameRepo
    private var gameDetailsLiveData: LiveData<ResultHandler<GamesRes>>?= null
    private val job = Job()

    fun getGameDetails(id: Int) {
        this.gameDetailsLiveData = gamersGeekLiveData {
            this.gamerGeekRepository.fetchGameById(id)
        }
    }

    fun getGameDetailsLiveData(): LiveData<ResultHandler<GamesRes>>? {
        return this.gameDetailsLiveData
    }

    fun storeGames(savedGame: SaveGames) {
        this.iSaveGameRepo.saveGames(savedGame)
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO


}