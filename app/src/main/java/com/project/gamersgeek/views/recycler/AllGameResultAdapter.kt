package com.project.gamersgeek.views.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.utils.GlideApp

class AllGameResultAdapter constructor(): PagedListAdapter<Results, AllGameResultAdapter.GameVideoViewHolder>(
    GAME_RESULT_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameVideoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_all_game_items_layout, parent, false)
        return GameVideoViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: GameVideoViewHolder, position: Int) {
        var results: Results?
        getItem(position).let {
            results = it
        }
        results?.let {
            holder.bindView(it)
        }
        holder.getPlayBackBt()?.let {
            it.setOnClickListener {
                if (holder.isPlaying) {
                    holder.pauseVideo()
                    holder.isPaused = true
                } else {
                    holder.playVideo()
                    holder.isPaused = false
                }
            }
        }
        holder.getVolumeBt()?.let {h ->
            h.setOnClickListener {
                holder.isMute?.let {isMute ->
                    if (isMute) {
                        holder.unmuteVideo()
                        holder.getVolumeBt()?.let {v ->
                            v.setImageResource(R.drawable.unmuteicon)
                        }
                    } else {
                        holder.muteVideo()
                        holder.getVolumeBt()?.let {v ->
                            v.setImageResource(R.drawable.muteicon)
                        }
                    }
                    holder.isMute = ! holder.isMute!!
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: GameVideoViewHolder) {
        resetHolder(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: GameVideoViewHolder) {
        resetHolder(holder)
        super.onViewRecycled(holder)
    }

    private fun resetHolder(holder: GameVideoViewHolder?) {
        holder?.let {aahHolder ->
            aahHolder.aah_vi?.let {
                it.customVideoView.clearAll()
                it.customVideoView.invalidate()
            }
        }
    }

    inner class GameVideoViewHolder constructor(private val view: View, private val context: Context): AAH_CustomViewHolder(view) {
        private var volumeBt: AppCompatImageView?= null
        private var playBackBt: AppCompatImageView?= null
        private var iconRecyclerView: RecyclerView?= null
        private var gameNameView: AppCompatTextView?= null
        var isMute: Boolean?= false

        init {
            this.volumeBt = this.view.findViewById(R.id.all_games_item_vol_bt_id)
            this.playBackBt = this.view.findViewById(R.id.all_game_items_playback_bt_id)
            this.iconRecyclerView = this.view.findViewById(R.id.game_icon_list_view_id)
            this.gameNameView = this.view.findViewById(R.id.game_title_id)
        }

        fun bindView(data: Results) {
            this.itemView.tag = data
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            data.videoClip.let {
                imageUrl = it.preview
                videoUrl = it.clip
                GlideApp.with(this.view).load(it.preview)
                    .placeholder(circularProgressDrawable)
                    .into(aaH_ImageView)
            }
            isLooping = true
        }

        override fun pauseVideo() {
            super.pauseVideo()
            this.playBackBt?.setImageResource(R.drawable.playicon)
        }

        override fun videoStarted() {
            super.videoStarted()
            this.playBackBt?.setImageResource(R.drawable.pauseicon)
            this.isMute?.let {
                if (it) {
                    muteVideo()
                    this.volumeBt?.let {v ->
                        switchVolumeBt(it, v)
                    }
                } else {
                    unmuteVideo()
                    this.volumeBt?.let {v ->
                        switchVolumeBt(it, v)
                    }
                }
            }
        }

        fun getVolumeBt(): AppCompatImageView? {
            return this.volumeBt
        }
        fun getPlayBackBt(): AppCompatImageView? {
            return this.playBackBt
        }
        fun getIconRecyclerView(): RecyclerView? {
            return this.iconRecyclerView
        }

        fun getGameNameView(): AppCompatTextView? {
            return this.gameNameView
        }
    }

    private fun switchVolumeBt(isMute: Boolean, volumeBt: AppCompatImageView) {
        if (isMute) {
            volumeBt.setImageResource(R.drawable.muteicon)
        } else {
            volumeBt.setImageResource(R.drawable.unmuteicon)
        }
    }

    companion object {
        private val GAME_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<Results>() {
            override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
                return oldItem == newItem
            }
        }
    }
}