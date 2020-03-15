package com.project.gamersgeek.utils

enum class EsrbRatingType(private val type: String) {
    EVERYONE("everyone"),
    EVERYONE_PLUS_10("everyone-10-plus"),
    TEEN("teen"),
    MATURE("mature"),
    ADULTS_ONLY("adults-only");

    fun getType(): String {
        return this.type
    }
}