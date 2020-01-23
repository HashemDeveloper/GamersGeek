package com.project.gamersgeek.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.models.platforms.PlatformRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.PlatformPageViewModel
import com.project.gamersgeek.views.recycler.PlatformDetailsAdapter
import com.project.gamersgeek.views.widgets.GlobalLoadingBar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_platforms_page.*
import timber.log.Timber
import javax.inject.Inject

class PlatformsPage : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val platformPageViewModel: PlatformPageViewModel by viewModels {
        this.viewModelFactory
    }
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
        val adapter = PlatformDetailsAdapter()
        platform_page_recycler_view_id.layoutManager = LinearLayoutManager(context!!)
        platform_page_recycler_view_id.adapter = adapter
        this.platformPageViewModel.fetchGamePlatforms.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
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
}
