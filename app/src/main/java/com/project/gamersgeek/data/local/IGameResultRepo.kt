package com.project.gamersgeek.data.local

import com.project.gamersgeek.models.games.Results

interface IGameResultRepo {
    fun getAllGameResult(): List<Results>?
}