package com.project.gamersgeek.data.local

import com.project.gamersgeek.models.games.SaveGames

interface ISaveGameRepo {
    fun saveGames(saveGames: SaveGames)
    fun getAllSavedGames(): List<SaveGames>?
    fun getGameById(id: Int): SaveGames?
    fun deleteGamesById(id: Int)
    fun deleteAllGames()
}