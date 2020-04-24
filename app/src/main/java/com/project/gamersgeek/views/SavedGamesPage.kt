package com.project.gamersgeek.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.viewmodels.SavedGamesViewModel
import com.project.gamersgeek.views.recycler.SavedGamesAdapter
import com.project.gamersgeek.views.recycler.items.DividerHeader
import com.project.gamersgeek.views.recycler.items.GameProfileHeader
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_saved_games_page.*
import javax.inject.Inject

class SavedGamesPage : Fragment(), Injectable, SavedGamesAdapter.SavedGamePageListener {
    private var savedGameAdapter: SavedGamesAdapter?= null
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val savedGamesViewMode: SavedGamesViewModel by viewModels {
        this.viewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_saved_games_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.savedGameAdapter = SavedGamesAdapter(this)
        fragment_saved_game_recycler_id.layoutManager = LinearLayoutManager(this.context!!)
        fragment_saved_game_recycler_id.adapter = this.savedGameAdapter
        val header = GameProfileHeader("", "PC")
        val divider = DividerHeader("Game Played")
        val list: MutableList<Any> = arrayListOf()
        list.add(header)
        list.add(divider)
        this.savedGameAdapter?.setData(list)
    }

    override fun onBackPressed() {
        this.activity?.onBackPressed()
    }
}
