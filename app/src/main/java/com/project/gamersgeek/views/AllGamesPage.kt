package com.project.gamersgeek.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.project.gamersgeek.R
import com.project.gamersgeek.di.Injectable
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.models.games.GameListRes
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.viewmodels.AllGamesPageViewModel
import com.project.gamersgeek.views.widgets.GlobalLoadingBar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_all_games_page.*
import timber.log.Timber
import javax.inject.Inject

class AllGamesPage: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val allGamesPageViewModel: AllGamesPageViewModel by viewModels {
        this.viewModelFactory
    }
    private val globalLoadingBar: GlobalLoadingBar by lazy {
        GlobalLoadingBar(fragment_welcome_page_loading_view_id!!, activity!!)
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
        this.allGamesPageViewModel.fetchAllGames()
        this.allGamesPageViewModel.fetchAllGamesLiveData?.observe(this.activity!!, gameListLiveDataObserver())
    }

    private fun gameListLiveDataObserver(): Observer<ResultHandler<GameListRes?>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {
                    this.globalLoadingBar.startLoading(true)
                }
                ResultHandler.Status.SUCCESS -> {
                    if (it.data is GameListRes) {
                        val gameData: GameListRes = it.data
                        Timber.d(gameData.description)
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
