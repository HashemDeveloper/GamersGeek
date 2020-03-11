package com.project.gamersgeek.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.viewmodels.GameDetailsPageViewModel
import com.project.gamersgeek.views.GameDetailsPageArgs.fromBundle
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game_details_page.*
import timber.log.Timber
import javax.inject.Inject

class GameDetailsPage : Fragment(), Injectable {
    private val gameDetails by lazy {
        fromBundle(arguments!!).gameDetailsPage
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val gameDetailsViewModel: GameDetailsPageViewModel by activityViewModels {
        this.viewModelFactory
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
        var gameId: Int?= null
        gameData?.let {result ->
            gameId = result.id
            gameTitle = result.name
            gameImage = result.backgroundImage
            result.videoClip.let {videoClip ->
                videoUrl = videoClip.clip
            }
        }
        gameId?.let {
            this.gameDetailsViewModel.getGameDetails(it)
        }
        this.gameDetailsViewModel.getGameDetailsLiveData()?.let {
            it.observe(viewLifecycleOwner) {resultHandler ->
               when (resultHandler.status) {

               }
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
            var imageUri = ""
            if ("" != it) {
                imageUri = it

            } else {
                gameData?.videoClip?.let {p ->
                    imageUri = p.preview
                }
            }
            GlideApp.with(this)
                .load(imageUri)
                .into(fragment_video_game_bg_image_view_id)
        }
    }
}
