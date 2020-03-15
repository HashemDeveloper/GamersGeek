package com.project.gamersgeek.views


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.GamesRes
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.games.ShortScreenShot
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.utils.RatingType
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.GameDetailsPageViewModel
import com.project.gamersgeek.views.GameDetailsPageArgs.fromBundle
import com.project.gamersgeek.views.recycler.GameDetailsItemAdapter
import com.project.gamersgeek.views.recycler.items.RawDescriptions
import com.project.gamersgeek.views.recycler.items.ScreenShots
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game_details_page.*
import javax.inject.Inject
import kotlin.math.roundToInt


class GameDetailsPage : Fragment(), Injectable {
    private val gameDetails by lazy {
        fromBundle(arguments!!).gameDetailsPage
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val gameDetailsViewModel: GameDetailsPageViewModel by activityViewModels {
        this.viewModelFactory
    }
    private var gameDetailsItemAdapter: GameDetailsItemAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_game_details_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game_details_information_recyclerview_id.layoutManager = LinearLayoutManager(this.context!!)
        this.gameDetailsItemAdapter = GameDetailsItemAdapter()
        game_details_information_recyclerview_id.adapter = this.gameDetailsItemAdapter
        setupGameData()
    }

    private fun setupGameData() {
//        description_view_id.textColor = ContextCompat.getColor(this.context!!, R.color.gray_500)
//        description_view_id.setTextSize(32f)
        val gameData: Results? = gameDetails
        var gameTitle: String?= null
        var videoUrl: String? = null
        var gameImage: String? = null
        var gameId: Int?= null
        var screenShots: ScreenShots?= null
        gameData?.let {result ->
            gameId = result.id
            gameTitle = result.name
            gameImage = result.backgroundImage
            result.videoClip.let {videoClip ->
                videoUrl = videoClip.clip
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
                val circularProgressDrawable = CircularProgressDrawable(this.context!!)
                when (resultHandler.status) {
                   ResultHandler.Status.LOADING -> {
//                       description_view_loading_bar_id.visibility = View.VISIBLE
                   }
                   ResultHandler.Status.SUCCESS -> {
                       if (resultHandler.data is GamesRes) {
                           val gameRes: GamesRes? = resultHandler.data
                           val gameDetailsDataList: MutableList<Any> = arrayListOf()
                           gameRes?.let {res ->
                               val playTime = "Play Time ${res.playTime} hrs"
                               average_play_time_view_id.text = playTime
                               game_details_released_view_id.text = res.released
//                               description_view_loading_bar_id.visibility = View.GONE
//                               description_view_id.text = res.descriptionRaw
                               circularProgressDrawable.strokeWidth = 5f
                               circularProgressDrawable.centerRadius = 30f
                               circularProgressDrawable.setColorSchemeColors(Color.GRAY)
                               circularProgressDrawable.start()
                               // adds description view items
                               val rawDescriptions = RawDescriptions(res.descriptionRaw, res.backgroundImageAdditional)
                               gameDetailsDataList.add(0, rawDescriptions)
                               screenShots?.let {s ->
                                   gameDetailsDataList.add(1, s)
                               }
                               this.gameDetailsItemAdapter?.setGameDetailsData(gameDetailsDataList)

//                               GlideApp.with(this).load(res.backgroundImageAdditional)
//                                   .placeholder(circularProgressDrawable)
//                                   .into(game_bg_view_1)
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
//                       description_view_loading_bar_id.visibility = View.GONE
                   }
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
