package com.project.gamersgeek.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.paging.NetworkState
import com.project.gamersgeek.utils.search.SearchResultWrapper
import com.project.gamersgeek.utils.search.SearchHelper
import com.project.gamersgeek.viewmodels.AllGamesPageViewModel
import com.project.gamersgeek.views.recycler.AllGameResultAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_all_games_page.*
import javax.inject.Inject


class AllGamesPage: Fragment(), Injectable, AllGameResultAdapter.GameResultClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var mLastQuery: String = ""
    private val allGamesPageViewModel: AllGamesPageViewModel by activityViewModels {
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
        setupDarkMode()
    }
    private fun setupDarkMode() {
        val isNightMode: Boolean = this.allGamesPageViewModel.getIsNightModeOn()
        if (isNightMode) {
            all_game_search_view_id.setBackgroundColor(ContextCompat.getColor(this.context!!, R.color.black))
        } else {
            all_game_search_view_id.setBackgroundColor(ContextCompat.getColor(this.context!!, R.color.white))
        }
    }
    private fun setupVideoRecyclerView() {
        all_game_swipe_refresh_layout_id?.isRefreshing = true
        val allGameAdapter = AllGameResultAdapter(this)
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
                all_game_swipe_refresh_layout_id?.isRefreshing = false
                allGameAdapter.submitList(it)
            }
        })
        swipeToRefresh()
        setupSearchFun(allGameAdapter)
    }

    private fun swipeToRefresh() {
        this.allGamesPageViewModel.refreshState.observe(viewLifecycleOwner) {
            all_game_swipe_refresh_layout_id.isRefreshing = it == NetworkState.LOADING
        }
        all_game_swipe_refresh_layout_id?.setOnRefreshListener {
            this.allGamesPageViewModel.refresh()
        }
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
                currentQuery?.let {
                    mLastQuery = it
                }
                val searchHelper = SearchHelper(mLastQuery, SearchResultWrapper.SearchByType.NAME)
                this@AllGamesPage.allGamesPageViewModel.onSearch(searchHelper)
                this@AllGamesPage.allGamesPageViewModel.getResultLiveData()?.let {liveData->
                    liveData.observe(viewLifecycleOwner, Observer {
                        if (it != null && it.size != 0) {
                            allGameAdapter.submitList(it)
                        }
                    })
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
               val wrapper: SearchResultWrapper = searchSuggestion as SearchResultWrapper
               val searchHelper = SearchHelper(wrapper.searchBody, SearchResultWrapper.SearchByType.NAME)
                this@AllGamesPage.allGamesPageViewModel.onSearch(searchHelper)
                this@AllGamesPage.allGamesPageViewModel.getResultLiveData()?.let {liveData->
                    liveData.observe(viewLifecycleOwner, Observer {
                        if (it != null && it.size != 0) {
                            allGameAdapter.submitList(it)
                        }
                    })
                }
                mLastQuery = searchHelper.searchBody
                Constants.hideKeyboard(activity)
            }
        })
        all_game_search_view_id.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocusCleared() {
                all_game_search_view_id.setSearchBarTitle(mLastQuery)
            }

            override fun onFocus() {
                this@AllGamesPage.allGamesPageViewModel.setupSearchHistory(all_game_search_view_id)
            }
        })
        all_game_search_view_id.setOnSuggestionsListHeightChanged {
            all_game_recycler_view_id.translationY = it
        }
        all_game_search_view_id.setOnBindSuggestionCallback { suggestionView, leftIcon, textView, item, itemPosition ->
            val searchResultWrapper: SearchResultWrapper = item as SearchResultWrapper
            val isNightModeOn: Boolean = this@AllGamesPage.allGamesPageViewModel.getIsNightModeOn()
            if (isNightModeOn) {
                suggestionView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
            } else {
                suggestionView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            }
            if (searchResultWrapper.isHistory) {
                leftIcon.alpha = 1.0f
                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(context?.resources!!, R.drawable.ic_history_gray_24dp, null))
            } else {
                leftIcon.alpha = 0.0f
                leftIcon.setImageDrawable(null)
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        Constants.hideKeyboard(activity)
    }
}
