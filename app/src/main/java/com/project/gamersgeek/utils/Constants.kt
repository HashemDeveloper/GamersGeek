package com.project.gamersgeek.utils

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned


class Constants {
    companion object {
        const val IS_NIGHT_MODE: String = "IS NIGHT MODE"
        const val CONNECTIVITY_ACTION: String = "android.net.conn.CONNECTIVITY_CHANGE"

        fun getFirstWord(input: String): String {
            for (i in input.indices) {
                if (input[i] == ' ') {
                    return input.substring(0, i)
                }
            }
            return input
        }
        fun fromHtml(html: String?): Spanned? {
            return when {
                html == null -> {
                    SpannableString("")
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
                }
                else -> {
                    Html.fromHtml(html)
                }
            }
        }
    }
}