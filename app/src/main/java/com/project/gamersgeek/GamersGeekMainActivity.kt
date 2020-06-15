package com.project.gamersgeek

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.gamersgeek.data.local.ISharedPrefService
import com.project.gamersgeek.di.viewmodel.ViewModelFactory
import com.project.gamersgeek.events.HamburgerEvent
import com.project.gamersgeek.events.NetworkStateEvent
import com.project.gamersgeek.models.localobj.NavigationHeaderItems
import com.project.gamersgeek.models.localobj.NavigationItems
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.navigateUriWithDefaultOptions
import com.project.gamersgeek.utils.networkconnections.IConnectionStateMonitor
import com.project.gamersgeek.viewmodels.PlatformPageViewModel
import com.project.gamersgeek.views.recycler.NavItemAdapter
import com.project.neardoc.rxeventbus.IRxEventBus
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gamers_geek_main_activity.*
import timber.log.Timber
import javax.inject.Inject

class GamersGeekMainActivity : AppCompatActivity(), HasSupportFragmentInjector, NavItemAdapter.DrawerClickListener{
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
    @Inject
    lateinit var iSharedPrefService: ISharedPrefService
    private lateinit var navController: NavController
    private var navItemAdapter: NavItemAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(R.style.AppTheme)
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
        monitorConnectionSetting()
        monitorThemeState()
    }
    private fun monitorThemeState() {
        val configuration: Configuration? = resources.configuration
        configuration?.let {config ->
            when (config.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    this.iSharedPrefService.setIsNightModeOn(false)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    this.iSharedPrefService.setIsNightModeOn(true)
                }
            }
        }
    }
    private fun setupNavBarItems() {
        val isNightModeOne: Boolean = this.iSharedPrefService.getIsNightModeOn()
        navigation_view_menu_item_view_id.layoutManager = LinearLayoutManager(this)
        this.navItemAdapter = NavItemAdapter(isNightModeOne, this)
        navigation_view_menu_item_view_id.adapter = navItemAdapter
        val backgroundImage: String = this.platformPageViewModel.getNavBackgroundImage()
        val navHeader = NavigationHeaderItems("", backgroundImage, "HashemDev")
        val creators = NavigationItems(R.drawable.ic_game_creators_24, "Creators")
        val stores = NavigationItems(R.drawable.ic_shopping_cart_gray_24dp, "Stores")
        val publisher = NavigationItems(R.drawable.game_publisher_icon, "Publisher")
        val developer = NavigationItems(R.drawable.game_developer_icon_black, "Developer")
        val navMenuList: MutableList<Any> = arrayListOf()
        navMenuList.add(navHeader)
        navMenuList.add(creators)
        navMenuList.add(stores)
        navMenuList.add(publisher)
        navMenuList.add(developer)
        this.navItemAdapter?.setData(navMenuList)
    }

    private fun setupNavDrawer() {
        this.compositeDisposable.add(this.iRxEventBus.observable(HamburgerEvent::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {event ->
                event?.floatingSearchView?.attachNavigationDrawerToMenuButton(navigation_drawer_layout_id)
                setupNavBarItems()
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
                this.platformPageViewModel.setupNetConnection(NetworkStateEvent(true))
            } else {
                this.platformPageViewModel.setupNetConnection(NetworkStateEvent(false))
            }
        })
    }

    private fun observeNotInternetConnectedLiveData(): Observer<Boolean> {
        return Observer {
            if (BuildConfig.DEBUG) {
                Timber.i("No Internet $it.toString()")
            }
        }
    }

    override fun onNavItemClicked(itemType: String) {
        when (itemType) {
            "Creators" -> {
                closeDrawer()
                this.navController.navigateUriWithDefaultOptions(Uri.parse(Constants.CREATOR_PAGE_NAV_URI))
            }
            "Stores" -> {
                closeDrawer()
            }
            "Publisher" -> {
                closeDrawer()
                this.navController.navigateUriWithDefaultOptions(Uri.parse(Constants.PUBLISHER_PAGE_NAV_URI))
            }
            "Developer" -> {
                closeDrawer()
                this.navController.navigateUriWithDefaultOptions(Uri.parse(Constants.DEVELOPER_PAGE_NAV_URI))
            }
        }
    }
    private fun closeDrawer() {
        if (navigation_drawer_layout_id.isDrawerOpen(GravityCompat.START)) {
            navigation_drawer_layout_id.closeDrawer(GravityCompat.START)
        }
    }
}
