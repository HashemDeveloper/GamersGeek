package com.project.gamersgeek.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import dagger.android.support.AndroidSupportInjection
import com.project.gamersgeek.views.PlatformDetailsPageArgs.fromBundle
import timber.log.Timber

class PlatformDetailsPage: Fragment(), Injectable {
    private val platformDetails by lazy {
        fromBundle(arguments!!).platformPage
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
       return inflater.inflate(R.layout.fragment_platform_details_page_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id: Int = this.platformDetails.id
        Timber.e("$id")
        super.onViewCreated(view, savedInstanceState)
    }
}