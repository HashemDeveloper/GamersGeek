package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Store
import com.project.gamersgeek.views.recycler.items.StoreListAdapter

class BottomSheetShopListAdapter constructor(private val listener: StoreListAdapter.StoreListClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val storeList: MutableList<Store> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_shop_list_item_layout, parent, false)
        val storeListViewHolder = StoreListViewHolder(view)
        storeListViewHolder.getShopListContainer()?.let { container ->
            container.setOnClickListener { click ->
                val store: Store = storeListViewHolder.itemView.tag as Store
                val url: String = store.englishUrl
                this.listener.onStoreClicked(url)
            }
        }
        return storeListViewHolder
    }

    override fun getItemCount(): Int {
        return this.storeList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder as StoreListViewHolder).bindView(this.storeList[position]))
    }

    fun setupStoreList(list: List<Store>) {
        this.storeList.clear()
        this.storeList.addAll(list)
        notifyDataSetChanged()
    }

    inner class StoreListViewHolder(private val view: View) : BaseViewHolder<Store>(view) {
        private var container: ConstraintLayout?= null
        private var storeImageView: AppCompatImageView? = null
        private var storeNameView: AppCompatTextView? = null

        init {
            this.storeImageView = this.view.findViewById(R.id.bottom_sheet_item_shop_icon_id)
            this.storeNameView = this.view.findViewById(R.id.bottom_sheet_item_shop_name_view_id)
            this.container = this.view.findViewById(R.id.bottom_sheet_list_item_container_id)
        }

        override fun bindView(item: Store) {
            this.itemView.tag = item
            val storeName: String = item.storeList.name
            val storeSlug: String = item.storeList.slug
            when (storeSlug) {
                "steam" -> {
                    setupIcons(R.drawable.steam_store_black_n_white, storeName, true)
                }
                "xbox-store" -> {
                    setupIcons(R.drawable.xbox_icon, storeName, true)
                }
                "playstation-store" -> {
                    setupIcons(R.drawable.play_station_icon, storeName, true)
                }
                "epic-games" -> {
                    setupIcons(R.drawable.epic_games_store, storeName, true)
                }
                "nintendo" -> {
                    setupIcons(R.drawable.nintendo_icon, storeName, true)
                }
                "google-play" -> {
                    setupIcons(R.drawable.google_playstore_icon_white, storeName, true)
                }
                "xbox360" -> {
                    setupIcons(R.drawable.xbox_icon, storeName, true)
                }
                "gog" -> {
                    setupIcons(R.drawable.gog_store_icon, storeName, true)
                }
                "apple-appstore" -> {
                    setupIcons(R.drawable.apple_store_white_icon, storeName, true)
                }
                else -> {
                    setupIcons(0, "", false)
                }
            }
        }

        private fun setupIcons(iconId: Int, storeName: String, isPlatformExists: Boolean) {
            if (isPlatformExists) {
                this.storeNameView?.visibility = View.VISIBLE
                this.storeNameView?.text = storeName
                this.storeImageView?.visibility = View.VISIBLE
                this.storeImageView?.setImageResource(iconId)
            } else {
                this.storeNameView?.visibility = View.GONE
                this.storeImageView?.visibility = View.GONE
            }
        }

        fun getShopListContainer(): ConstraintLayout? {
            return this.container
        }
    }
}