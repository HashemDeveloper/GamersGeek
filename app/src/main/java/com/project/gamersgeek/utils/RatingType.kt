package com.project.gamersgeek.utils

enum class RatingType(private val type: String) {

    RECOMMENDED("recommended"),
    MEH("meh"),
    EXCEPTIONAL("exceptional"),
    SKIP("skip");

    fun getType(): String {
        return this.type
    }
}