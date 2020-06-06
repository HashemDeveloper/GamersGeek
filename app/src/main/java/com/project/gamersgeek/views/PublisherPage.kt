package com.project.gamersgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.project.gamersgeek.R
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.publishers.DevPublisherInfoResponse
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.PublisherPageViewModel
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

class PublisherPage : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val publisherPageViewModel: PublisherPageViewModel by activityViewModels {
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
        return inflater.inflate(R.layout.fragment_publisher_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.publisherPageViewModel.fetchPublisherList()
        this.publisherPageViewModel.publisherLiveData?.observe(viewLifecycleOwner, developerListObserver())
    }
    private fun developerListObserver(): Observer<ResultHandler<DevPublisherInfoResponse>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {
                    Timber.e("Loading")
                }
                ResultHandler.Status.SUCCESS -> {
                    val data: DevPublisherInfoResponse? = it.data as DevPublisherInfoResponse
                    data?.let { d ->
                        Timber.e("${d.count}")
                    }
                }
                ResultHandler.Status.ERROR -> {
                    val error: String? = it.message
                    Timber.e("$error")
                }
            }
        }
    }
}
