package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Store
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.DividerHeader
import com.project.gamersgeek.views.recycler.items.GamePlayed
import com.project.gamersgeek.views.recycler.items.GameProfileHeader
import com.project.gamersgeek.views.recycler.items.WishToPlay
import java.lang.IllegalArgumentException

class SavedGamesAdapter (private val listener: SavedGamePageListener, private val gamePlayedOrNotListener: GamePlayedOrNotAdapter.GamePlayedOrNotListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<Any> = arrayListOf()
    private var isNightMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            GAME_PROFILE_HEADER -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_platform_view_layout, parent, false)
                val profileHolder = GameProfileHeaderHolder(view, parent.context, getIsNightMode())
                profileHolder.getBackBt()?.setOnClickListener {
                    this.listener.onBackPressed()
                }
                profileHolder
            }
            DIVIDER_HEADER -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_item_header_layout, parent, false)
                DividerHeaderHolder(view)
            }
            GAME_PLAYED_VIEW -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_played_layout, parent, false)
                GamePlayedViewHolder(view, parent.context, getIsNightMode(), this.gamePlayedOrNotListener)
            }
            GAME_WISH_TO_PLAY -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_wish_to_play_layout, parent, false)
                WishToPlayViewHolder(view, parent.context, this.isNightMode, this.gamePlayedOrNotListener)
            }
            else -> {
                throw IllegalArgumentException("Unsupported view")
            }
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item: Any = this.data[position]
        when (holder) {
            is GameProfileHeaderHolder -> holder.bindView(item as GameProfileHeader)
            is DividerHeaderHolder -> holder.bindView(item as DividerHeader)
            is GamePlayedViewHolder -> holder.bindView(item as GamePlayed)
            is WishToPlayViewHolder -> holder.bindView(item as WishToPlay)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is GameProfileHeader -> GAME_PROFILE_HEADER
            is DividerHeader -> DIVIDER_HEADER
            is GamePlayed -> GAME_PLAYED_VIEW
            is WishToPlay -> GAME_WISH_TO_PLAY
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }
    fun setIsNightMode(isNightMode: Boolean) {
        this.isNightMode = isNightMode
    }
    private fun getIsNightMode(): Boolean {
        return this.isNightMode
    }

    fun setData(data: List<Any>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
    inner class DividerHeaderHolder(private val view: View): BaseViewHolder<DividerHeader>(view) {
        private var titleView: MaterialTextView?= null
        init {
            this.titleView = this.view.findViewById(R.id.fragment_saved_game_page_header_title_view_id)
        }

        override fun bindView(item: DividerHeader) {
            this.titleView?.let { textView ->
                textView.text = item.title
            }
        }
    }
    inner class GameProfileHeaderHolder(
        private val view: View,
        private val context: Context,
        private val isNightMode: Boolean
    ): BaseViewHolder<GameProfileHeader>(view) {
        private var platformImageView: AppCompatImageView?= null
        private var backBtView: AppCompatImageView?= null
        private var platformTitle: MaterialTextView?= null

        init {
            this.platformImageView = this.view.findViewById(R.id.fragment_saved_game_platform_image_view_id)
            this.backBtView = this.view.findViewById(R.id.fragment_saved_game_back_bt_id)
            this.platformTitle = this.view.findViewById(R.id.fragment_saved_game_player_name_view_id)
            if (this.isNightMode) {
                this.platformImageView?.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY)
                this.platformTitle?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            } else {
                this.platformImageView?.setColorFilter(ContextCompat.getColor(this.context, R.color.black), PorterDuff.Mode.MULTIPLY)
                this.backBtView?.setColorFilter(ContextCompat.getColor(this.context, R.color.black), PorterDuff.Mode.MULTIPLY)
                this.platformTitle?.setTextColor(ContextCompat.getColor(this.context, R.color.black))
            }
        }

        override fun bindView(item: GameProfileHeader) {
            val circularProgressDrawable: CircularProgressDrawable = getCircularProgressDr(this.context)
            circularProgressDrawable.start()
            this.platformImageView?.let { platformView ->
                GlideApp.with(this.view)
                    .load(item.gamePlatformImage)
                    .placeholder(circularProgressDrawable)
                    .into(platformView)
            }
            this.platformTitle?.let { titleView ->
                val text = "${item.platformName} Gamer"
                titleView.text = text
            }
        }
        fun getBackBt(): AppCompatImageView? {
            return this.backBtView
        }
    }

    inner class GamePlayedViewHolder(private val view: View, private val context:
    Context, private val isNightMode: Boolean,
                                     private val listener: GamePlayedOrNotAdapter.GamePlayedOrNotListener): BaseViewHolder<GamePlayed>(view) {
        private var gamePlayedRecyclerView: RecyclerView?= null
        private var gamePlayedOrNotAdapter: GamePlayedOrNotAdapter?= null
        private var noItemView: MaterialTextView?= null

        init {
            this.gamePlayedRecyclerView = this.view.findViewById(R.id.fragment_saved_game_played_page_recycler_view_id)
            this.noItemView = this.view.findViewById(R.id.fragment_saved_game_played_page_no_item_view_id)
            this.gamePlayedRecyclerView?.layoutManager = LinearLayoutManager(this.context)
            this.gamePlayedOrNotAdapter = GamePlayedOrNotAdapter(this.isNightMode, true, this.listener)
            this.gamePlayedRecyclerView?.adapter = this.gamePlayedOrNotAdapter
        }
        override fun bindView(item: GamePlayed) {
            if (item.saveGameList?.isNotEmpty()!!) {
                this.gamePlayedRecyclerView?.visibility = View.VISIBLE
                this.noItemView?.visibility = View.GONE
                item.saveGameList?.let { list ->
                    this.gamePlayedOrNotAdapter?.setData(list)
                }
            } else {
                this.noItemView?.visibility = View.VISIBLE
                this.gamePlayedRecyclerView?.visibility = View.GONE
            }
        }
    }
    inner class WishToPlayViewHolder(private val view: View,
                                     private val context: Context,
                                     private val isNightMode: Boolean,
                                     private val listener: GamePlayedOrNotAdapter.GamePlayedOrNotListener): BaseViewHolder<WishToPlay>(view) {
        private var wishToPlayRecyclerView: RecyclerView?= null
        private var gamePlayedOrNotAdapter: GamePlayedOrNotAdapter?= null
        private var noItemView: MaterialTextView?= null
        init {
            this.wishToPlayRecyclerView = this.view.findViewById(R.id.fragment_saved_game_page_wish_to_play_recycler_view_id)
            this.noItemView = this.view.findViewById(R.id.fragment_saved_game_wish_page_no_item_view_id)
            this.wishToPlayRecyclerView?.layoutManager = LinearLayoutManager(this.context)
            this.gamePlayedOrNotAdapter = GamePlayedOrNotAdapter(this.isNightMode, false, this.listener)
            this.wishToPlayRecyclerView?.adapter = this.gamePlayedOrNotAdapter
        }

        override fun bindView(item: WishToPlay) {
            if (item.saveGameList?.isNotEmpty()!!) {
                this.wishToPlayRecyclerView?.visibility = View.VISIBLE
                this.noItemView?.visibility = View.GONE
                item.saveGameList?.let { list ->
                    this.gamePlayedOrNotAdapter?.setData(list)
                }
            } else {
                this.noItemView?.visibility = View.VISIBLE
                this.wishToPlayRecyclerView?.visibility = View.GONE
            }
        }
    }
    private fun getCircularProgressDr(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(Color.GRAY)
        return circularProgressDrawable
    }

    interface SavedGamePageListener {
        fun onBackPressed()
    }
    companion object {
        private const val GAME_PROFILE_HEADER = 0
        private const val DIVIDER_HEADER = 1
        private const val GAME_PLAYED_VIEW = 2
        private const val GAME_WISH_TO_PLAY = 3
    }
}