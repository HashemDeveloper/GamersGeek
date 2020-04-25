package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.games.SaveGames
import com.project.gamersgeek.models.games.Store
import com.project.gamersgeek.models.games.StoreList
import com.project.gamersgeek.utils.GlideApp

class GamePlayedOrNotAdapter(private val isNightMode: Boolean,
                             private val isPlayed: Boolean,
                             private val listener: GamePlayedOrNotListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_played_item_layout, parent, false)
        val viewHolder = GamePlayedItemViewHolder(view, parent.context, this.isNightMode, this.isPlayed)
        viewHolder.getImageView()?.setOnClickListener {
            val saveGames: SaveGames = viewHolder.itemView.tag as SaveGames
            this.listener.imageClicked(saveGames.gameResult)
        }
        viewHolder.getShopBt()?.setOnClickListener {
            val saveGames: SaveGames = viewHolder.itemView.tag as SaveGames
            this.listener.onShopBtClicked(saveGames.storeList)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val saveGames: SaveGames = this.data[position] as SaveGames
        (holder as GamePlayedItemViewHolder).bindView(saveGames)
    }

    fun setData(list: List<SaveGames>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    inner class GamePlayedItemViewHolder(
        private val view: View,
        private val context: Context,
        private val isNightMode: Boolean, private val isPlayed: Boolean): BaseViewHolder<SaveGames>(view) {
        private var imageView: AppCompatImageView?= null
        private var gameTitleView: MaterialTextView?= null
        private var shopBt: AppCompatImageView?= null
        init {
            this.imageView = this.view.findViewById(R.id.fragment_saved_game_image_bg_view_id)
            this.gameTitleView = this.view.findViewById(R.id.fragment_saved_game_title_view_id)
            this.shopBt = this.view.findViewById(R.id.fragment_saved_game_shop_bt_id)
            if (this.isPlayed) {
                this.shopBt?.visibility = View.GONE
            } else {
                this.shopBt?.visibility = View.VISIBLE
            }
            if (this.isNightMode) {
                this.gameTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                this.gameTitleView?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
                this.shopBt?.setColorFilter(ContextCompat.getColor(this.context, R.color.red), PorterDuff.Mode.MULTIPLY)
            }
        }
        override fun bindView(item: SaveGames) {
            itemView.tag = item
            val circularProgressDrawable: CircularProgressDrawable = getCircularProgressDr(this.context)
            circularProgressDrawable.start()
            this.imageView?.let { image ->
                GlideApp.with(this.view)
                    .load(item.backgroundImage)
                    .placeholder(circularProgressDrawable)
                    .into(image)
            }
            this.gameTitleView?.let { titleView ->
                titleView.text = item.name
            }
        }
        fun getImageView(): AppCompatImageView? {
            return this.imageView
        }
        fun getShopBt(): AppCompatImageView? {
            return this.shopBt
        }
    }
    private fun getCircularProgressDr(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(Color.GRAY)
        return circularProgressDrawable
    }
    interface GamePlayedOrNotListener {
        fun onShopBtClicked(storeList: List<Store>?)
        fun imageClicked(gameResult: Results?)
    }
}