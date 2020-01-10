package com.project.gamersgeek.views


import android.os.Bundle
import android.util.Log
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
import com.project.gamersgeek.viewmodels.WelcomePageViewModel
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

class WelcomePage : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val welcomePageViewModel: WelcomePageViewModel by viewModels {
        this.viewModelFactory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.welcomePageViewModel.fetchAllGames()
        this.welcomePageViewModel.fetchAllGamesLiveData?.observe(this.activity!!, gameListLiveDataObserver())
    }

    private fun gameListLiveDataObserver(): Observer<ResultHandler<GameListRes?>> {
        return Observer {
            when (it.status) {
                ResultHandler.Status.LOADING -> {

                }
                ResultHandler.Status.SUCCESS -> {
                    if (it.data is GameListRes) {
                        val gameData: GameListRes = it.data
                        Timber.d(gameData.description)
                    }
                }
                ResultHandler.Status.ERROR -> {

                }
            }
        }
    }
}
