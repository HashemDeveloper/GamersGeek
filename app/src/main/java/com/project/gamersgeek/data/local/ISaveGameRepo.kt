package com.project.gamersgeek.data.local

import com.project.gamersgeek.models.games.SaveGames

interface ISaveGameRepo {
    fun saveGames(saveGames: SaveGames)
    fun getPlayedGames(): List<SaveGames>?
    fun getWishedToPlayGames(): List<SaveGames>?
    fun getGameById(id: Int): SaveGames?
    fun deleteGamesById(id: Int)
    fun deleteAllGames()
}