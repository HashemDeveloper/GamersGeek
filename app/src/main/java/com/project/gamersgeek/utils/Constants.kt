package com.project.gamersgeek.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
        fun getCurrentTime(): OffsetDateTime {
            val currentDate = Date()
            val dateFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy, kk:mm", Locale.getDefault())
            val dateGameSaved: String = dateFormat.format(currentDate)
            val dateTimeFormatter: DateTimeFormatter = org.threeten.bp.format.DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern("MMM dd, uuuu, kk:mm")
                .toFormatter()
                .withResolverStyle(org.threeten.bp.format.ResolverStyle.STRICT)
            val localDate: LocalDate = LocalDate.parse(dateGameSaved, dateTimeFormatter)
            return OffsetDateTime.of(localDate, LocalTime.now(), ZoneOffset.UTC)
        }
        fun hideKeyboard(activity: FragmentActivity?) {
            val imm: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }
}