package com.project.gamersgeek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.project.gamersgeek.events.HamburgerEvent
import com.project.neardoc.rxeventbus.IRxEventBus
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gamers_geek_main_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class GamersGeekMainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
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

    override fun onStart() {
        super.onStart()
        setupNavDrawer()
    }

    private fun setupNavDrawer() {
        this.compositeDisposable.add(this.iRxEventBus.observable(HamburgerEvent::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {event ->
                event?.floatingSearchView?.attachNavigationDrawerToMenuButton(navigation_drawer_layout_id)
            })
    }

    override fun onBackPressed() {
        if (navigation_drawer_layout_id.isDrawerOpen(GravityCompat.START)) {
            navigation_drawer_layout_id.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.dispatchFragmentInjector
    }
}
