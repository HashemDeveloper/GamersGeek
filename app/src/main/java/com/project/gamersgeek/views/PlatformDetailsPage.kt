package com.project.gamersgeek.views

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.PlatformDetailsPageViewModel
import dagger.android.support.AndroidSupportInjection
import com.project.gamersgeek.views.PlatformDetailsPageArgs.fromBundle
import com.project.gamersgeek.views.recycler.PlatformDetailsAdapter
import com.project.gamersgeek.views.recycler.items.GameListWrapper
import kotlinx.android.synthetic.main.fragment_platform_details_page_layout.*
import javax.inject.Inject

class PlatformDetailsPage: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val detailPageViewModel: PlatformDetailsPageViewModel by activityViewModels {
        this.viewModelFactory
    }
    private val platformDetails by lazy {
        fromBundle(arguments!!).platformPage
    }
    private var platformDetailsAdapter: PlatformDetailsAdapter?= null
    private var isNightModeOn: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_platform_details_page_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.isNightModeOn = this.detailPageViewModel.isNightModeOne()
        this.platformDetailsAdapter = PlatformDetailsAdapter(this.isNightModeOn)
        fragment_platform_details_page_recycler_view_id.layoutManager = LinearLayoutManager(this.context!!)
        fragment_platform_details_page_recycler_view_id?.adapter = this.platformDetailsAdapter
        val id: Int = this.platformDetails.id
        val gameListWrapper = GameListWrapper(this.platformDetails.games)
        this.detailPageViewModel.loadPlatformDetails(id)
        this.detailPageViewModel.platformDetailLiveData?.observe(viewLifecycleOwner, platformResLiveDataObserver(gameListWrapper))
        super.onViewCreated(view, savedInstanceState)
        if (this.isNightModeOn) {
            fragment_platform_details_page_progress_bar_id?.indeterminateDrawable?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
        }
    }

    private fun platformResLiveDataObserver(gameListWrapper: GameListWrapper?=null): Observer<ResultHandler<PlatformDetails>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {
                    showProgressBar(true)
                }
                ResultHandler.Status.SUCCESS -> {
                    showProgressBar(false)
                    val data: PlatformDetails = it.data as PlatformDetails
                    val list: MutableList<Any> = arrayListOf()
                    list.add(data)
                    gameListWrapper?.let {wrapper ->
                        list.add(wrapper)
                    }
                    this.platformDetailsAdapter?.add(list)
                }
                ResultHandler.Status.ERROR -> {
                    showProgressBar(false)
                    Toast.makeText(this.context, "Server error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showProgressBar(isVisible: Boolean) {
        fragment_platform_details_page_progress_bar_id?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.detailPageViewModel.platformDetailLiveData?.removeObserver(platformResLiveDataObserver())
    }
}