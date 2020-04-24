package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.DividerHeader
import com.project.gamersgeek.views.recycler.items.GameProfileHeader
import kotlinx.android.synthetic.main.fragment_saved_game_page_item_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_saved_game_page_played_layout.view.*
import java.lang.IllegalArgumentException

class SavedGamesAdapter (private val listener: SavedGamePageListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            GAME_PROFILE_HEADER -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_platform_view_layout, parent, false)
                val profileHolder = GameProfileHeaderHolder(view, parent.context)
                profileHolder.getBackBt()?.setOnClickListener {
                    this.listener.onBackPressed()
                }
                profileHolder
            }
            DIVIDER_HEADER -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_game_page_item_header_layout, parent, false)
                DividerHeaderHolder(view)
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
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is GameProfileHeader -> GAME_PROFILE_HEADER
            is DividerHeader -> DIVIDER_HEADER
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
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
    inner class GameProfileHeaderHolder(private val view: View, private val context: Context): BaseViewHolder<GameProfileHeader>(view) {
        private var platformImageView: AppCompatImageView?= null
        private var backBtView: AppCompatImageView?= null
        private var platformTitle: MaterialTextView?= null

        init {
            this.platformImageView = this.view.findViewById(R.id.fragment_saved_game_platform_image_view_id)
            this.backBtView = this.view.findViewById(R.id.fragment_saved_game_back_bt_id)
            this.platformTitle = this.view.findViewById(R.id.fragment_saved_game_title_view_id)
        }

        override fun bindView(item: GameProfileHeader) {
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.GRAY)
            circularProgressDrawable.start()
            this.platformImageView?.let { platformView ->
                GlideApp.with(this.view)
                    .load(item.gamePlatformImage)
                    .placeholder(circularProgressDrawable)
                    .into(platformView)
            }
            this.platformTitle?.let { titleView ->
                titleView.text = item.platformName
            }
        }
        fun getBackBt(): AppCompatImageView? {
            return this.backBtView
        }
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