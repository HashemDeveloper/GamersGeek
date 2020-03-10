package com.project.gamersgeek.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.views.GameDetailsPageArgs.fromBundle
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game_details_page.*
import timber.log.Timber

class GameDetailsPage : Fragment(), Injectable {
    private val gameDetails by lazy {
        fromBundle(arguments!!).gameDetailsPage
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_game_details_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameData: Results? = gameDetails
        var videoUrl: String? = null
        gameData?.let {result ->
            result.videoClip.let {videoClip ->
                videoUrl = videoClip.clip
            }
        }
        videoUrl?.let {url ->
            fragment_video_player_view_id.setSource(url)
        }
    }
}
