package com.project.gamersgeek.utils

import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.StyleSpan
import androidx.appcompat.widget.AppCompatTextView


class Constants {
    companion object {
        const val IS_NIGHT_MODE: String = "IS NIGHT MODE"
        const val CONNECTIVITY_ACTION: String = "android.net.conn.CONNECTIVITY_CHANGE"
        const val YOUTUBE_VIDEO_URL: String = "https://www.youtube.com/watch?v="

        fun getFirstWord(input: String): String {
            for (i in input.indices) {
                if (input[i] == ' ') {
                    return input.substring(0, i)
                }
            }
            return input
        }
        fun boldFirstWord(end: Int, sentence: String, textView: AppCompatTextView) {
            val fancySentence = SpannableStringBuilder(sentence)
            fancySentence.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = fancySentence
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