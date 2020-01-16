package com.project.gamersgeek.views.widgets

import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.project.gamersgeek.R

abstract class BaseGlobalLoadingBar {
    protected fun startLoading(container: View?, activity: FragmentActivity, isLoading: Boolean) {
        val lottieAnimationView: LottieAnimationView?= container!!.findViewById(R.id.lotti_global_loading_bar_id)
        if (isLoading) {
            container.visibility = View.VISIBLE
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            lottieAnimationView?.playAnimation()
        } else {
            container.visibility = View.GONE
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            lottieAnimationView?.cancelAnimation()
        }
    }
}