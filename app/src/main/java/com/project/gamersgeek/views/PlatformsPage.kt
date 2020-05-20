package com.project.gamersgeek.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.paging.NetworkState
import com.project.gamersgeek.viewmodels.PlatformPageViewModel
import com.project.gamersgeek.views.recycler.PlatformDetailsAdapter
import com.project.gamersgeek.views.widgets.GlobalLoadingBar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_platforms_page.*
import timber.log.Timber
import javax.inject.Inject

class PlatformsPage : Fragment(), Injectable, PlatformDetailsAdapter.PlatformDetailsListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val platformPageViewModel: PlatformPageViewModel by activityViewModels {
        this.viewModelFactory
    }
    private var isNetConnected: Boolean = false
    private val globalLoadingBar: GlobalLoadingBar by lazy {
        GlobalLoadingBar(platform_page_loading_view_id!!, activity!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_platforms_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PlatformDetailsAdapter(this)
        platform_page_recycler_view_id.layoutManager = LinearLayoutManager(context!!)
        platform_page_recycler_view_id.adapter = adapter
        this.platformPageViewModel.platformDetailsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            val position = (platform_page_recycler_view_id.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (position != RecyclerView.NO_POSITION) {
                platform_page_recycler_view_id.scrollToPosition(position)
            }
            platform_page_swipe_to_refresh_layout_id.isRefreshing = false
        }
        this.platformPageViewModel.networkState.observe(viewLifecycleOwner) {
            adapter.setNetworkState(it)
        }
        swipeToRefresh()
        setupDrawer()
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
        this.platformPageViewModel.setupDrawer(platform_page_search_id)
    }

    private fun gameListLiveDataObserver(): Observer<ResultHandler<PlatformRes?>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {
                    this.globalLoadingBar.startLoading(true)
                }
                ResultHandler.Status.SUCCESS -> {
                    if (it.data is PlatformRes) {
                        val platformData: PlatformRes = it.data
                        for (result in platformData.listOfResult) {
                            Timber.e("Name: ${result.name}")
                        }
                    }
                    this.globalLoadingBar.startLoading(false)
                }
                ResultHandler.Status.ERROR -> {
                    this.globalLoadingBar.startLoading(false)
                }
            }
        }
    }

    override fun onPlatformViewClicked(platformDetails: PlatformDetails) {
        val platformDetailsRouter: PlatformsPageDirections.ActionPlatformDetailsPage = PlatformsPageDirections.actionPlatformDetailsPage(platformDetails)
        val controller: NavController = findNavController()
        controller.navigate(platformDetailsRouter)
    }

    override fun onShowGameClicked(gameId: Int, showGameType: PlatformDetailsAdapter.ShowGameType) {
        Timber.e("GameId: $gameId")
    }
}
