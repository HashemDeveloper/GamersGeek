package com.project.gamersgeek.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.project.gamersgeek.R
import dagger.android.support.AndroidSupportInjection

class GameDetailsPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_game_details_page, container, false)
    }


}
