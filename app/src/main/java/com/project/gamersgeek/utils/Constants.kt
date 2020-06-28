package com.project.gamersgeek.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.style.StyleSpan
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Constants {
    companion object {
        const val SEARCH_FOR_ALL_GAMES: String = "AllGames"
        const val SEARCH_FOR_PLATFOR: String = "Platform"
        val SAVED_GAME_PLATFORM_HEADER: String = "SAVED_GAME_PLATFORM_IMAGE"
        const val IS_NIGHT_MODE: String = "IS NIGHT MODE"
        const val CONNECTIVITY_ACTION: String = "android.net.conn.CONNECTIVITY_CHANGE"
        const val YOUTUBE_VIDEO_URL: String = "https://www.youtube.com/watch?v="
        const val PUBLISHER_PAGE_NAV_URI = "gamersgeek://publisherpage"
        const val DEVELOPER_PAGE_NAV_URI = "gamersgeek://developerpage"
        const val CREATOR_PAGE_NAV_URI = "gamersgeek://createrpage"
        const val STORE_PAGE_NAV_URI = "gamersgeek://storepage"

        fun changeIconColor(context: Context, iconId: Int, iconColor: Int) {
            val drawable: Drawable? = ContextCompat.getDrawable(context, iconId)?.mutate()
            drawable?.let { d ->
                val wrapDrawable: Drawable = DrawableCompat.wrap(d)
                DrawableCompat.setTint(wrapDrawable, iconColor)
            }
        }

        fun glideCircularAnim(context: Context): CircularProgressDrawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.GRAY)
            return circularProgressDrawable
        }
        fun beautifyString(input: String): String {
            val clearNonLetters: String = input.replace("&#39;", "'")
            val clearFirstTag: String = clearNonLetters.replace("<p>", "")
            val finalDesc: String = clearFirstTag.replace("</p>", "").replace("<br />", "").replace("<br>", "")
            return finalDesc
        }
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

        fun removeLastUnwantedChar(value: String, char: Char): String? {
            var v: String? = value
            if (v != null && v.isNotEmpty() && v[v.length - 1] == char) {
                v = v.substring(0, v.length - 1)
            }
            return v
        }
        fun capitalizeFirstCharInWord(value: String?, target: Char): String? {
            val sb = StringBuilder(value?.length!!)
            var isCapital = true
            for (i in value.indices) {
                var char: Char = value[i]
                if (char == target) {
                    isCapital = true
                } else if (isCapital) {
                    char = char.toUpperCase()
                    isCapital = false
                }
                sb.append(char)
            }
            return sb.toString()
        }

        fun getExpirationTime(type: ExpirationType, expireTime: Long): Long {
            var result: Long = 0
            val now = Date()
            when (type) {
                ExpirationType.DEFAULT -> {
                    result = now.time - TimeUnit.HOURS.toMillis(6)
                }
                ExpirationType.MINUTES -> {
                    result = now.time - TimeUnit.MINUTES.toMillis(expireTime)
                }
                ExpirationType.HOURS -> {
                    result = now.time - TimeUnit.HOURS.toMillis(expireTime)
                }
                ExpirationType.DAY -> {
                    result = now.time - TimeUnit.DAYS.toMillis(expireTime)
                }
                ExpirationType.NEVER -> {
                    result = 0
                }
            }
            return result
        }
        fun toggleDrawer(drawerLayout: DrawerLayout?) {
           drawerLayout?.let { drawer ->
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START)
                } else {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
        }

        enum class ExpirationType {
            DEFAULT,
            MINUTES,
            HOURS,
            DAY,
            NEVER
        }
    }
}