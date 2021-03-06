package com.project.gamersgeek.views


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.games.SaveGames
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.utils.RatingType
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.GameDetailsPageViewModel
import com.project.gamersgeek.views.GameDetailsPageArgs.fromBundle
import com.project.gamersgeek.views.recycler.GameDetailsItemAdapter
import com.project.gamersgeek.views.recycler.items.*
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game_details_page.*
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt


class GameDetailsPage : Fragment(), Injectable, GameDetailsItemAdapter.GameDetailsClickListener {
    private val gameDetails by lazy {
        fromBundle(requireArguments()).gameDetailsPage
    }
    var alertDialog: AlertDialog?= null
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val gameDetailsViewModel: GameDetailsPageViewModel by activityViewModels {
        this.viewModelFactory
    }
    private var gameDetailsItemAdapter: GameDetailsItemAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_details_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(fragment_video_player_view_id)
        game_details_information_recyclerview_id.layoutManager = LinearLayoutManager(requireContext())
        this.gameDetailsItemAdapter = GameDetailsItemAdapter(this, this.gameDetails)
        game_details_information_recyclerview_id.adapter = this.gameDetailsItemAdapter
        setupGameData()
    }

    private fun setupGameData() {
        val gameData: Results? = gameDetails
        var gameTitle: String?= null
        var videoIdOfYoutube: String? = null
        var gameImage: String? = null
        var gameId: Int?= null
        var screenShots: ScreenShots?= null
        gameData?.let {result ->
            gameId = result.id
            gameTitle = result.name
            gameImage = result.backgroundImage
            result.videoClip.let {videoClip ->
                videoIdOfYoutube = videoClip?.video
            }
            // adds screen shots
            result.shortScreenShotList?.let {
                screenShots = ScreenShots(it)
            }
        }
        gameId?.let {
            this.gameDetailsViewModel.getGameDetails(it)
        }
        this.gameDetailsViewModel.getGameDetailsLiveData()?.let {
            it.observe(viewLifecycleOwner) {resultHandler ->
                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                when (resultHandler.status) {
                   ResultHandler.Status.LOADING -> {
                       description_view_loading_bar_id.visibility = View.VISIBLE
                   }
                   ResultHandler.Status.SUCCESS -> {
                       description_view_loading_bar_id.visibility = View.GONE
                       if (resultHandler.data is GamesRes) {
                           val gameRes: GamesRes? = resultHandler.data
                           val gameDetailsDataList: MutableList<Any> = arrayListOf()
                           gameRes?.let {res ->
                               val playTime = "Play Time ${res.playTime} hrs"
                               average_play_time_view_id.text = playTime
                               game_details_released_view_id.text = res.released
                               circularProgressDrawable.strokeWidth = 5f
                               circularProgressDrawable.centerRadius = 30f
                               circularProgressDrawable.setColorSchemeColors(Color.GRAY)
                               circularProgressDrawable.start()
                               // adds description view items
                               val rawDescriptions = RawDescriptions(res.descriptionRaw, res.backgroundImageAdditional)
                               gameDetailsDataList.add(0, rawDescriptions)
                               // adds screen shots
                               screenShots?.let {s ->
                                   gameDetailsDataList.add(1, s)
                               }
                               // ads developer info and genres
                               res.developers?.let {devList ->
                                   res.genres?.let {genresList ->
                                       val devAndGenres = GameDevAndGenres(devList, genresList)
                                       gameDetailsDataList.add(devAndGenres)
                                   }
                               }
                               // adds platfor details to display pc requirements
                               gameData?.platformList?.let {platformList ->
                                   val pcRequirements = PcRequirements(platformList)
                                   gameDetailsDataList.add(pcRequirements)
                               }

                               // add where to buy
                               gameData?.listOfStores?.let {storeList ->
                                   val stores = GameDetailsStores(storeList)
                                   gameDetailsDataList.add(stores)
                               }

                               // ads footer
                               var esrbRatingName = ""
                               res.esrbRating?.let {es ->
                                   esrbRatingName = es.slug
                               }

                               val gameDetailsFooter = GameDetailsFooter(
                                   res.id, res.name,
                                   res.website, esrbRatingName,
                                   this.gameDetails,
                                   gameData?.listOfStores, res.backgroundImage,
                                   res.backgroundImageAdditional)

                               gameDetailsDataList.add(gameDetailsFooter)

                               this.gameDetailsItemAdapter?.setGameDetailsData(gameDetailsDataList)

                               res.ratingList?.let { ratingList ->
                                   var exceptionalPercentile: Int?= null
                                   var recommendedPercentile: Int?= null
                                   var mehPercentile: Int?= null
                                   var skipPercentile: Int?= null
                                   for (ratings in ratingList) {
                                       when (ratings.title) {
                                           RatingType.EXCEPTIONAL.getType() -> {
                                               exceptionalPercentile = ratings.percent.roundToInt()
                                           }
                                           RatingType.RECOMMENDED.getType() -> {
                                               recommendedPercentile = ratings.percent.roundToInt()
                                           }
                                           RatingType.MEH.getType() -> {
                                               mehPercentile = ratings.percent.roundToInt()
                                           }
                                           RatingType.SKIP.getType() -> {
                                               skipPercentile = ratings.percent.roundToInt()
                                           }
                                       }
                                   }
                                   exceptionalPercentile?.let { value ->
                                       val percent = "$value %"
                                       exception_text_view_id.text = percent
                                   }
                                   recommendedPercentile?.let {value ->
                                       val percent = "$value %"
                                       recommeded_text_view_id.text = percent
                                   }
                                   mehPercentile?.let {value ->
                                       val percent = "$value %"
                                       meh_text_view_id.text = percent
                                   }
                                   skipPercentile?.let {value ->
                                       val percent = "$value %"
                                       bad_text_view_id.text = percent
                                   }
                               }
                           }
                       }
                   }
                   ResultHandler.Status.ERROR -> {
                       description_view_loading_bar_id.visibility = View.GONE
                   }
               }
            }
        }
        gameTitle?.let {
            fragment_game_details_game_title_view_id?.let {
                it.text = gameTitle
            }
        }
        var videoIdFinal = ""
        videoIdOfYoutube?.let { videoId ->
            videoIdFinal = videoId
        }
        fragment_video_player_view_id.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                fragment_video_player_view_id.enterFullScreen()
                youTubePlayer.loadVideo(videoIdFinal, 0f)
            }

            override fun onError(
                youTubePlayer: YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                if (BuildConfig.DEBUG) {
                    Timber.d("Error $error")
                }
            }
        })
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

    override fun <T> onGameDetailsItemClicked(items: T) {
        when (items) {
            is String -> {
                val storeUrl: String = items
                if (storeUrl.isNotEmpty()) {
                    val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl))
                    startActivity(browseIntent)
                }
            }
            is GameDetailsFooter -> {
                val gameDetailsFooter: GameDetailsFooter = items
                storeGames(gameDetailsFooter)
            }
        }
    }
    private fun storeGames(gameDetailsFooter: GameDetailsFooter) {
        promptChoice(gameDetailsFooter)
    }
    private fun promptChoice(gameDetailsFooter: GameDetailsFooter) {
        var saveGames: SaveGames?
        var isPlayed = false
        val choices: Array<CharSequence> = arrayOf("I've played this game.", "I wish to play.")
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        alertBuilder.setTitle(gameDetailsFooter.name)
        alertBuilder.setSingleChoiceItems(choices, -1) { dialog, item ->
            when (item) {
                0 -> {
                    isPlayed = true
                }
                1 -> {
                    isPlayed = false
                }
            }
            val date: OffsetDateTime = Constants.getCurrentTime()
            var name: String = ""
            gameDetailsFooter.name?.let {gameName ->
                name = gameName
            }
            saveGames = SaveGames(gameDetailsFooter.id, name, date, isPlayed,
                gameDetailsFooter.gameResult,
                gameDetailsFooter.storeList, gameDetailsFooter.backgroundImage1, gameDetailsFooter.backgroundImage2)
            saveGames?.let {
                this.gameDetailsViewModel.storeGames(it)
            }
            dialog.dismiss()
        }
        alertDialog = alertBuilder.create()
        alertBuilder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.alertDialog?.dismiss()
    }
}
