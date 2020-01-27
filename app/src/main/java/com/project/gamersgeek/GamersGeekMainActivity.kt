package com.project.gamersgeek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.gamers_geek_main_activity.*
import javax.inject.Inject

class GamersGeekMainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchFragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamers_geek_main_activity)
        this.navController = Navigation.findNavController(this, R.id.container)
        this.navController.setGraph(R.navigation.gamers_geek_nav_layout)
        bottom_nav_bar_id?.setupWithNavController(this.navController)
        navigation_view_id?.setupWithNavController(this.navController)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.dispatchFragmentInjector
    }
}
