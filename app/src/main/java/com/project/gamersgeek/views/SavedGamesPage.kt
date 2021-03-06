package com.project.gamersgeek.views


import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.games.Store
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.viewmodels.SavedGamesViewModel
import com.project.gamersgeek.views.recycler.GamePlayedOrNotAdapter
import com.project.gamersgeek.views.recycler.SavedGamesAdapter
import com.project.gamersgeek.views.recycler.items.DividerHeader
import com.project.gamersgeek.views.recycler.items.GamePlayed
import com.project.gamersgeek.views.recycler.items.GameProfileHeader
import com.project.gamersgeek.views.recycler.items.WishToPlay
import com.project.gamersgeek.views.widgets.GamersGeekBottomSheet
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_saved_games_page.*
import javax.inject.Inject

class SavedGamesPage : Fragment(), Injectable, SavedGamesAdapter.SavedGamePageListener,
    SharedPreferences.OnSharedPreferenceChangeListener, GamePlayedOrNotAdapter.GamePlayedOrNotListener {
    private var gamersGeekBottomSheet: GamersGeekBottomSheet?= null
    private var savedGameAdapter: SavedGamesAdapter?= null
    private val list: MutableList<Any> = arrayListOf()
    var alertDialog: AlertDialog?= null
    private var gamePlayed: GamePlayed?= null
    private var wishToPlay: WishToPlay?= null
    private var isNightMode = false
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val savedGamesViewModel: SavedGamesViewModel by activityViewModels {
        this.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_games_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.savedGamesViewModel.setupSharedPrefListener(this)
        this.gamePlayed = this.savedGamesViewModel.getGamePlayedList()
        this.wishToPlay = this.savedGamesViewModel.getWishToPlayList()
        this.isNightMode = this.savedGamesViewModel.getIsNightModeOn()
        this.savedGameAdapter = SavedGamesAdapter(this, this)
        this.savedGameAdapter?.setIsNightMode(this.isNightMode)
        fragment_saved_game_recycler_id.layoutManager = LinearLayoutManager(requireContext())
        fragment_saved_game_recycler_id.adapter = this.savedGameAdapter
        val platformHeader: GameProfileHeader? = this.savedGamesViewModel.getPlatformImage()
        if (platformHeader == null) {
           promptChoice(platformHeader)
        } else {
            val divider = DividerHeader("Game Played")
            this.list.add(platformHeader)
            this.list.add(divider)
            this.gamePlayed?.let { playedGames ->
                this.list.add(playedGames)
            }
            val divider1 = DividerHeader("Wish To Play")
            list.add(divider1)
            this.wishToPlay?.let { wishGames ->
                this.list.add(wishGames)
            }
            this.savedGameAdapter?.setData(list)
        }
    }

    private fun promptChoice(profileHeader: GameProfileHeader?) {
        val choices: Array<CharSequence> = arrayOf("PC", "Playstation", "Xbox", "Nintendo", "Android", "Mac")
        var gameProfileHeader: GameProfileHeader?= profileHeader
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        alertBuilder.setTitle("I am a gamer on: ")
        alertBuilder.setSingleChoiceItems(choices, -1) { dialog, item ->
            when (item) {
                0 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.microsoft_windows_icon, "PC")
                }
                1 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.play_station_icon, "Playstation")
                }
                2 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.xbox_icon, "Xbox")
                }
                3 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.nintendo_icon, "Nintendo")
                }
                4 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.android_icon, "Android")
                }
                5 -> {
                    gameProfileHeader = GameProfileHeader(R.drawable.mac_icon, "Mac")
                }
            }
            gameProfileHeader?.let { header ->
                this.savedGamesViewModel.saveProfileHeader(header)
            }
            dialog.dismiss()
        }
        this.alertDialog = alertBuilder.create()
        alertBuilder.show()
    }

    override fun onBackPressed() {
        this.activity?.onBackPressed()
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        when (key) {
            Constants.SAVED_GAME_PLATFORM_HEADER -> {
                val gson = Gson()
                val headerObj: String = pref?.getString(Constants.SAVED_GAME_PLATFORM_HEADER, "")!!
                val gameProfileHeader: GameProfileHeader = gson.fromJson(headerObj, GameProfileHeader::class.java)
                this.list.add(gameProfileHeader)
                val divider = DividerHeader("Game Played")
                this.list.add(divider)
                this.gamePlayed?.let { playedGames ->
                    this.list.add(playedGames)
                }
                this.wishToPlay?.let { wishGames ->
                    this.list.add(wishGames)
                }
                val divider1 = DividerHeader("Wish To Play")
                list.add(divider1)
                this.savedGameAdapter?.setData(list)
            }
        }
    }

    override fun onShopBtClicked(storeList: List<Store>?) {
        storeList?.let { list ->
            this.gamersGeekBottomSheet = GamersGeekBottomSheet(list, this.isNightMode)
            this.gamersGeekBottomSheet?.show(requireActivity().supportFragmentManager, this.gamersGeekBottomSheet?.tag)
            this.gamersGeekBottomSheet?.getClickObserver()?.observe(requireActivity(), shopListClickListener())
        }
    }

    private fun shopListClickListener(): Observer<String> {
        return Observer {
            val storeUrl: String = it
            if (storeUrl.isNotEmpty()) {
                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl))
                startActivity(browseIntent)
                this.gamersGeekBottomSheet?.dismiss()
            }
        }
    }

    override fun imageClicked(gameResult: Results?) {
        gameResult?.let {
            val gameDetailPageRouter: SavedGamesPageDirections.ActionBottomNavSavedGameIdToGameDetailsPage2 = SavedGamesPageDirections.actionBottomNavSavedGameIdToGameDetailsPage2(it)
            val gameResultController: NavController = findNavController()
            gameResultController.navigate(gameDetailPageRouter)
            this.list.clear()
        }
    }
}
