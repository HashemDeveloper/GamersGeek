package com.project.gamersgeek.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.NonNull
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.BaseRequestOptions
import kotlin.math.roundToInt


@GlideExtension
object GamersGeekGlideExtension {

    @NonNull
    @JvmStatic
    @GlideOption
    fun roundedCorners(options: BaseRequestOptions<*>, context: Context, cornerRadius: Int): BaseRequestOptions<*> {
        val px =
            (cornerRadius * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        return options.transforms(RoundedCorners(px))
    }
}