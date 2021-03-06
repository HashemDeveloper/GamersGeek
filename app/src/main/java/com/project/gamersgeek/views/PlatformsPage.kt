package com.project.gamersgeek.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.paging.NetworkState
import com.project.gamersgeek.utils.search.SearchResultWrapper
import com.project.gamersgeek.utils.search.SearchHelper
import com.project.gamersgeek.viewmodels.PlatformPageViewModel
import com.project.gamersgeek.viewmodels.SharedViewModel
import com.project.gamersgeek.views.recycler.PlatformAdapter
import com.project.gamersgeek.views.widgets.GlobalLoadingBar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_platforms_page.*
import timber.log.Timber
import javax.inject.Inject

class PlatformsPage : Fragment(), Injectable, PlatformAdapter.PlatformListener {
    private var mLastQuery: String = ""
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val platformPageViewModel: PlatformPageViewModel by activityViewModels {
        this.viewModelFactory
    }
    private val sharedViewModel: SharedViewModel by activityViewModels {
        this.viewModelFactory
    }
    private var isNetConnected: Boolean = false
    private val globalLoadingBar: GlobalLoadingBar by lazy {
        GlobalLoadingBar(platform_page_loading_view_id!!, requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_platforms_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlatformViewList()
    }

    private fun setupPlatformViewList() {
        val adapter = PlatformAdapter(this)
        platform_page_recycler_view_id.layoutManager = LinearLayoutManager(requireContext())
        platform_page_recycler_view_id.adapter = adapter
        this.platformPageViewModel.platformDetailsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            val position = (platform_page_recycler_view_id.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (position != RecyclerView.NO_POSITION) {
                platform_page_recycler_view_id.scrollToPosition(position)
            }
            platform_page_swipe_to_refresh_layout_id.isRefreshing = false
        }
        this.platformPageViewModel.textFilterLiveData.value = null
        this.platformPageViewModel.networkState.observe(viewLifecycleOwner) {
            adapter.setNetworkState(it)
        }
        swipeToRefresh()
        setupDrawer()
        setupSearchFunctionality(adapter)
    }

    private fun setupSearchFunctionality(adapter: PlatformAdapter) {
        platform_page_search_id?.setOnQueryChangeListener {oldQuery, newQuery ->
            if (oldQuery.isNotEmpty() && newQuery.isEmpty()) {
                platform_page_search_id?.clearSuggestions()
            } else {
                this.platformPageViewModel.findSuggestions(newQuery, platform_page_search_id)
            }
        }
        platform_page_search_id?.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                currentQuery?.let {
                    mLastQuery = it
                }
                val searchHelper: SearchHelper = SearchHelper(mLastQuery, SearchResultWrapper.SearchByType.NAME)
                this@PlatformsPage.platformPageViewModel.onSearch(searchHelper)

            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupDarkMode() {
        val isNightModeOn: Boolean = this.platformPageViewModel.getIsNightModeOn()
        platform_page_search_id?.setBackgroundColor(if (isNightModeOn) ContextCompat.getColor(requireContext(), R.color.black) else ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun swipeToRefresh() {
        this.platformPageViewModel.refreshState.observe(viewLifecycleOwner) {
            platform_page_swipe_to_refresh_layout_id.isRefreshing = it == NetworkState.LOADING
        }
        platform_page_swipe_to_refresh_layout_id.setOnRefreshListener {
            this.platformPageViewModel.refresh()
        }
    }
    private fun setupDrawer() {
        fragment_platform_page_menu_bt_id?.setOnClickListener { bt->
            this.sharedViewModel.toggleDrawer()
        }
    }

    override fun onPlatformViewClicked(platformDetails: PlatformDetails) {
        val platformDetailsRouter: PlatformsPageDirections.ActionPlatformDetailsPage = PlatformsPageDirections.actionPlatformDetailsPage(platformDetails)
        val controller: NavController = findNavController()
        controller.navigate(platformDetailsRouter)
    }

    override fun onResume() {
        super.onResume()
        setupDarkMode()
    }

    override fun onShowGameClicked(gameId: Int, showGameType: PlatformAdapter.ShowGameType) {
        Timber.e("GameId: $gameId")
    }
}
