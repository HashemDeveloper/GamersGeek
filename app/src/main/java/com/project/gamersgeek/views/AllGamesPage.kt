package com.project.gamersgeek.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.GenericPlatformDetails
import com.project.gamersgeek.utils.search.GameResultWrapper
import com.project.gamersgeek.utils.search.SearchHelper
import com.project.gamersgeek.viewmodels.AllGamesPageViewModel
import com.project.gamersgeek.views.recycler.AllGameResultAdapter
import com.project.gamersgeek.views.recycler.PlatformIconAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_all_games_page.*
import timber.log.Timber
import javax.inject.Inject

class AllGamesPage: Fragment(), Injectable, AllGameResultAdapter.GameResultClickListener, PlatformIconAdapter.PlatformIconClickListener{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val allGamesPageViewModel: AllGamesPageViewModel by viewModels {
        this.viewModelFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_all_games_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVideoRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupDrawer()
    }

    private fun setupVideoRecyclerView() {
        val allGameAdapter = AllGameResultAdapter(this, this)
        all_game_recycler_view_id.layoutManager = LinearLayoutManager(this.context)
        all_game_recycler_view_id.setActivity(activity)
        all_game_recycler_view_id.setPlayOnlyFirstVideo(true)
        all_game_recycler_view_id.setCheckForMp4(false)
        all_game_recycler_view_id.setVisiblePercent(50f)
        all_game_recycler_view_id.adapter = allGameAdapter
        all_game_recycler_view_id.smoothScrollBy(0, 1)
        all_game_recycler_view_id.smoothScrollBy(0, -1)
        this.allGamesPageViewModel.textFilterLiveData.value = null
        this.allGamesPageViewModel.gameResultLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null && it.size != 0) {
                allGameAdapter.submitList(it)
            }
        })
        setupSearchFun(allGameAdapter)
    }

    private fun setupSearchFun(allGameAdapter: AllGameResultAdapter) {
        all_game_search_view_id.setOnQueryChangeListener { oldQuery, newQuery ->
            if (oldQuery.isNotEmpty() && newQuery.isEmpty()) {
                all_game_search_view_id.clearSuggestions()
            } else {
                this.allGamesPageViewModel.findSuggestions(newQuery, all_game_search_view_id)
            }
        }

        all_game_search_view_id.setOnSearchListener(object : FloatingSearchView.OnSearchListener{
            override fun onSearchAction(currentQuery: String?) {
                TODO("Not yet implemented")
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
               val wrapper: GameResultWrapper = searchSuggestion as GameResultWrapper
               val searchHelper: SearchHelper
                searchHelper = when (wrapper.searchType) {
                    GameResultWrapper.SearchByType.NAME -> {
                        SearchHelper(wrapper.searchBody, GameResultWrapper.SearchByType.NAME)
                    }
                    GameResultWrapper.SearchByType.PLATFORM -> {
                        SearchHelper(wrapper.searchBody, GameResultWrapper.SearchByType.PLATFORM)
                    }
                }
                this@AllGamesPage.allGamesPageViewModel.onSearch(searchHelper)
                this@AllGamesPage.allGamesPageViewModel.getResultLiveData()?.let {liveData->
                    liveData.observe(viewLifecycleOwner, Observer {
                        if (it != null && it.size != 0) {
                            allGameAdapter.submitList(it)
                        }
                    })
                }
            }
        })
    }

    private fun setupDrawer() {
        this.allGamesPageViewModel.setupDrawer(all_game_search_view_id)
    }

    override fun onStop() {
        super.onStop()
        all_game_recycler_view_id?.stopVideos()
    }

    override fun onVideoClicked(results: Results, type: AllGameResultAdapter.VideoItemClickType) {
        when (type) {
            AllGameResultAdapter.VideoItemClickType.EXPAND_VIDEO -> {
                val gameDetailPageRouter: AllGamesPageDirections.ActionGameDetailsPage = AllGamesPageDirections.actionGameDetailsPage(results)
                val gameResultController: NavController = findNavController()
                gameResultController.navigate(gameDetailPageRouter)
            }
        }
    }

    override fun onPlatformIconClicked(platform: GenericPlatformDetails) {
        Timber.e("Platform: ${platform.name}")
    }
}
