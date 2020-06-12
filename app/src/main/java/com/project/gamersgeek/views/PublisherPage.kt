package com.project.gamersgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.project.gamersgeek.R
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.PublisherPageViewModel
import com.project.gamersgeek.views.recycler.DevPubPageAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_publisher_list_layout.*
import timber.log.Timber
import javax.inject.Inject

class PublisherPage : Fragment() {
    private var pubPageAdapter: DevPubPageAdapter?= null
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
        setupListeners()
        this.pubPageAdapter = DevPubPageAdapter()
        fragment_publisher_page_recycler_view_id?.layoutManager = LinearLayoutManager(this.context)
        fragment_publisher_page_recycler_view_id.adapter = this.pubPageAdapter
        this.publisherPageViewModel.fetchPublisherList()
        this.publisherPageViewModel.publisherLiveData?.observe(viewLifecycleOwner, developerListObserver())
    }
    private fun setupListeners() {
        fragment_pub_page_back_bt?.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    private fun developerListObserver(): Observer<ResultHandler<BaseResModel<DevPubResult>>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {
                    Timber.e("Loading")
                }
                ResultHandler.Status.SUCCESS -> {
                    val data: BaseResModel<DevPubResult>? = it.data as BaseResModel<DevPubResult>
                    data?.let { d ->
                        val list: MutableList<DevPubResult> = arrayListOf()
                        d.results?.let {r ->
                            list.addAll(r)
                        }
                        this.pubPageAdapter?.setData(list)
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
