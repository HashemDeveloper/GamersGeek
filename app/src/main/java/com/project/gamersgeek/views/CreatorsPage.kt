package com.project.gamersgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.gamersgeek.BuildConfig

import com.project.gamersgeek.R
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.CreatorPageViewModel
import com.project.gamersgeek.views.recycler.CreatorPageAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_creators_page.*
import timber.log.Timber
import javax.inject.Inject

class CreatorsPage : Fragment(), CreatorPageAdapter.CreatorPageListener {
    @Inject
    lateinit var viewModelFactor: ViewModelFactory
    private val creatorPageViewModel: CreatorPageViewModel by activityViewModels {
        this.viewModelFactor
    }
    private var creatorPageAdapter: CreatorPageAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_creators_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.creatorPageAdapter = CreatorPageAdapter(this)
        fragment_creator_page_recycler_view_id?.layoutManager = LinearLayoutManager(requireContext())
        fragment_creator_page_recycler_view_id?.adapter = this.creatorPageAdapter
        this.creatorPageViewModel.fetchCreatorsList()
        this.creatorPageViewModel.resultLiveData.observe(viewLifecycleOwner, creatorListObserver())
    }
    private fun creatorListObserver(): Observer<ResultHandler<BaseResModel<CreatorResults>>> {
        return Observer {handler ->
            when (handler.status) {
                ResultHandler.Status.LOADING -> {
                    displayProgressBar(true)
                }
                ResultHandler.Status.SUCCESS -> {
                    displayProgressBar(false)
                    if (handler.data is BaseResModel<CreatorResults>) {
                        val response: BaseResModel<CreatorResults> = handler.data
                        val listOfPosition: List<CreatorResults>?= response.results
                        listOfPosition?.let { list ->
                            if (list.isNotEmpty()) {
                                this.creatorPageAdapter?.setData(list)
                            }
                        }
                    }
                }
                ResultHandler.Status.ERROR -> {
                    displayProgressBar(false)
                    val errorMessage: String? = handler.message
                    if (BuildConfig.DEBUG) {
                        Timber.e(errorMessage)
                    }
                    Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayProgressBar(isDisplay: Boolean) {
        if (isDisplay) fragment_creator_page_progress_bar_id.visibility = View.VISIBLE else fragment_creator_page_progress_bar_id.visibility = View.GONE
    }

    override fun onCardClicked(creatorInfo: CreatorResults) {
        Timber.e("${creatorInfo.id_}")
    }
}
