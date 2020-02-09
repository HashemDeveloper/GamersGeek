package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.localobj.NavigationHeaderItems
import com.project.gamersgeek.models.localobj.NavigationItems
import com.project.gamersgeek.utils.GlideApp
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.IllegalArgumentException


class NavItemAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            NAV_HEADER -> {
                val headerView: View = LayoutInflater.from(parent.context).inflate(R.layout.navigation_drawer_header_layout, parent, false)
                NavHeaderViewHolder(headerView)
            }
            NAV_ITEMS -> {
                val navItemView: View = LayoutInflater.from(parent.context).inflate(R.layout.nav_drawer_items_layout, parent, false)
                NavItemViewHolder(navItemView)
            } else -> throw IllegalArgumentException("Unsupported view")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
      val items: Any = this.data[position]
        when (holder) {
            is NavHeaderViewHolder -> holder.bindView(items as NavigationHeaderItems)
            is NavItemViewHolder -> holder.bindView(items as NavigationItems)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is NavigationHeaderItems -> NAV_HEADER
            is NavigationItems -> NAV_ITEMS
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }

    fun setData(dataList: MutableList<Any>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class NavHeaderViewHolder(private val view: View): BaseViewHolder<NavigationHeaderItems>(view) {
        private var backgroundImageView: AppCompatImageView?= null
        private var userImageView: CircleImageView?= null
        private var usernameView: AppCompatTextView?= null

        init {
            this.backgroundImageView = this.view.findViewById(R.id.nav_header_background_image_view)
            this.userImageView = this.view.findViewById(R.id.nav_header_user_image_view)
            this.usernameView = this.view.findViewById(R.id.navigation_user_name_view_id)
        }
        override fun bindView(item: NavigationHeaderItems) {
            item.let {
                this.backgroundImageView?.let {bgImage ->
                    if (it.backgroundImage.isNotEmpty()) {
                        GlideApp.with(this.view).load(it.backgroundImage).into(bgImage)
                    } else {
                        bgImage.setImageResource(R.drawable.pc_gaming_bg)
                    }
                }
                this.userImageView?.let {uImage ->
                    if (it.profileImage.isNotEmpty()) {
                        uImage.visibility = View.VISIBLE
                        GlideApp.with(this.view).load(it.profileImage).into(uImage)
                    } else {
                        uImage.visibility = View.GONE
                    }
                }
                this.usernameView?.let {name ->
                    if (it.userName.isNotEmpty()) {
                        name.visibility = View.VISIBLE
                        name.text = it.userName
                    } else {
                        name.visibility = View.GONE
                    }
                }
            }
        }

        fun getBackgroundImageView(): AppCompatImageView? {
            return this.backgroundImageView
        }
        fun getUserImageView(): CircleImageView? {
            return this.userImageView
        }
    }
    inner class NavItemViewHolder(private val view: View): BaseViewHolder<NavigationItems>(view) {
        private var itemContainerView: ConstraintLayout?= null
        private var itemIconView: AppCompatImageView?= null
        private var itemTitleView: AppCompatTextView?= null

        init {
            this.itemContainerView = this.view.findViewById(R.id.nav_item_container_id)
            this.itemIconView = this.view.findViewById(R.id.nav_item_icon_view_id)
            this.itemTitleView = this.view.findViewById(R.id.nav_item_setting_name_view_id)
        }
        override fun bindView(item: NavigationItems) {
            item.let {i ->
                this.itemIconView?.setImageResource(i.itemIcon())
                this.itemTitleView?.let {
                    it.text = i.itemTitle()
                }
            }
        }
        fun getNavItemContainer(): ConstraintLayout? {
            return this.itemContainerView
        }
    }

    companion object {
        private const val NAV_HEADER: Int = 0
        private const val NAV_ITEMS: Int = 1
        private const val NAV_FOOTER: Int = 2
    }
}