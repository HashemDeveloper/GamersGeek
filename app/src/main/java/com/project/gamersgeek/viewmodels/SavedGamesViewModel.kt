package com.project.gamersgeek.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.data.local.ISaveGameRepo
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.models.games.SaveGames
import com.project.gamersgeek.views.recycler.items.GamePlayed
import com.project.gamersgeek.views.recycler.items.GameProfileHeader
import com.project.gamersgeek.views.recycler.items.WishToPlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SavedGamesViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var iSharedPrefService: ISharedPrefService
    @Inject
    lateinit var iSavedGameRepo: ISaveGameRepo

    private val job = Job()

    fun getPlatformImage(): GameProfileHeader? {
        val gson = Gson()
        var profileHeader: GameProfileHeader?= null
        try {
            val json: String = this.iSharedPrefService.getSavedGamePlatformImage()
            profileHeader = gson.fromJson(json, GameProfileHeader::class.java)
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.d(ex.localizedMessage)
            }
        }
        return profileHeader
    }

    fun saveProfileHeader(gameProfileHeader: GameProfileHeader) {
        val gson = Gson()
        val json: String = gson.toJson(gameProfileHeader)
        this.iSharedPrefService.storeGameProfileHeader(json)
    }

    fun setupSharedPrefListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.iSharedPrefService.registerOnSharedPrefListener(listener)
    }

    fun getIsNightModeOn(): Boolean {
        return this.iSharedPrefService.getIsNightModeOn()
    }

    fun getGamePlayedList(): GamePlayed? {
        var gamePlayed: GamePlayed?= null
        val list: List<SaveGames>? = this.iSavedGameRepo.getPlayedGames()
        list?.let { l ->
            gamePlayed = GamePlayed(l)
        }
        return gamePlayed
    }
    fun getWishToPlayList(): WishToPlay? {
        var wishToPlay: WishToPlay?= null
        val list: List<SaveGames>?= this.iSavedGameRepo.getWishedToPlayGames()
        list?.let { l ->
            wishToPlay = WishToPlay(l)
        }
        return wishToPlay
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

}