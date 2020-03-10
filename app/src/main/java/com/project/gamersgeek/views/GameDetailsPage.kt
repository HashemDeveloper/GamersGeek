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
import com.project.gamersgeek.utils.GlideApp
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
        setupGameData()
    }

    private fun setupGameData() {
        val gameData: Results? = gameDetails
        var gameTitle: String?= null
        var videoUrl: String? = null
        var gameImage: String? = null
        gameData?.let {result ->
            gameTitle = result.name
            result.videoClip.let {videoClip ->
                videoUrl = videoClip.clip
                gameImage = videoClip.preview
            }
        }
        gameTitle?.let {
            fragment_game_details_game_title_view_id?.let {
                it.text = gameTitle
            }
        }
        videoUrl?.let {url ->
            fragment_video_player_view_id.setSource(url)
        }
        gameImage?.let {
            GlideApp.with(this)
                .load(it)
                .into(fragment_video_game_bg_image_view_id)
        }
    }
}
