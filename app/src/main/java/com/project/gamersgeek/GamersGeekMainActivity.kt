package com.project.gamersgeek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.models.localobj.NavigationHeaderItems
import com.project.gamersgeek.models.localobj.NavigationItems
import com.project.gamersgeek.views.recycler.NavItemAdapter
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.neardoc.rxeventbus.IRxEventBus
import com.project.gamersgeek.utils.networkconnections.IConnectionStateMonitor
import com.project.gamersgeek.utils.networkconnections.GamersGeekNetworkType
import com.project.gamersgeek.viewmodels.PlatformPageViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gamers_geek_main_activity.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

class GamersGeekMainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val platformPageViewModel: PlatformPageViewModel by viewModels {
        this.viewModelFactory
    }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    @Inject
    lateinit var iRxEventBus: IRxEventBus
    @Inject
    lateinit var iConnectionStateMonitor: IConnectionStateMonitor
    @Inject
    lateinit var dispatchFragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var navController: NavController
    private var navItemAdapter: NavItemAdapter?= null
    private var isWifiConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamers_geek_main_activity)
        this.navController = Navigation.findNavController(this, R.id.container)
        this.navController.setGraph(R.navigation.gamers_geek_nav_layout)
        bottom_nav_bar_id?.setupWithNavController(this.navController)
        navigation_view_id?.setupWithNavController(this.navController)
        navigation_view_menu_item_view_id.layoutManager = LinearLayoutManager(this)
        this.navItemAdapter = NavItemAdapter()
        navigation_view_menu_item_view_id.adapter = navItemAdapter
        val navHeader = NavigationHeaderItems("", "", "HashemDev")
        val publisher = NavigationItems(R.drawable.game_publisher_icon, "Publisher")
        val developer = NavigationItems(R.drawable.game_developer_icon_black, "Developer")
        val navMenuList: MutableList<Any> = arrayListOf()
        navMenuList.add(navHeader)
        navMenuList.add(publisher)
        navMenuList.add(developer)
        this.navItemAdapter?.setData(navMenuList)
    }

    override fun onStart() {
        super.onStart()
        setupNavDrawer()
        monitorConnectionSetting()
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

    private fun monitorConnectionSetting() {
        this.iConnectionStateMonitor.getObserver().observe(this, Observer {isNetAvailable ->
            if (isNetAvailable) {
                this.iConnectionStateMonitor.isConnectedNoInternetLiveData().observe(this, observeNotInternetConnectedLiveData())
                this.iConnectionStateMonitor.isUsingWifiLiveData().observe(this, observeUsingWifiLiveData())
                this.iConnectionStateMonitor.isUsingMobileData().observe(this, observeUsingMobileDataLiveData())
                if (isWifiConnected) {
                    this.platformPageViewModel.setupNetConnection(NetworkStateEvent(true, GamersGeekNetworkType.WIFI_DATA))
                } else {
                    this.platformPageViewModel.setupNetConnection(NetworkStateEvent(true, GamersGeekNetworkType.MOBILE_DATA))
                }
            } else {
                this.platformPageViewModel.setupNetConnection(NetworkStateEvent(false, GamersGeekNetworkType.NO_NETWORK))
                Toast.makeText(this, "Connection lost", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeUsingWifiLiveData(): Observer<Boolean> {
        return Observer {isWifi ->
            if (isWifi) {
                this.isWifiConnected = true
            }
        }
    }
    private fun observeUsingMobileDataLiveData(): Observer<Boolean> {
        return Observer {isMobileData ->
            if (isMobileData) {
                this.isWifiConnected = false
            }
        }
    }
    private fun observeNotInternetConnectedLiveData(): Observer<Boolean> {
        return Observer {
            if (BuildConfig.DEBUG) {
                Timber.i("No Internet $it.toString()")
            }
        }
    }
}
