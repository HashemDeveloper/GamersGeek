package com.project.gamersgeek.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.PlatformDetailsPageViewModel
import dagger.android.support.AndroidSupportInjection
import com.project.gamersgeek.views.PlatformDetailsPageArgs.fromBundle
import timber.log.Timber
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
       return inflater.inflate(R.layout.fragment_platform_details_page_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id: Int = this.platformDetails.id
        this.detailPageViewModel.loadPlatformDetails(id)
        this.detailPageViewModel.platformDetailLiveData?.observe(viewLifecycleOwner, platformResLiveDataObserver())
        super.onViewCreated(view, savedInstanceState)
    }

    private fun platformResLiveDataObserver(): Observer<ResultHandler<PlatformDetails>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {

                }
                ResultHandler.Status.SUCCESS -> {
                    val data: PlatformDetails = it.data as PlatformDetails
                    Timber.e(Constants.beautifyString(data.description!!))
                }
                ResultHandler.Status.ERROR -> {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.detailPageViewModel.platformDetailLiveData?.removeObserver(platformResLiveDataObserver())
    }
}