package com.project.gamersgeek.views.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.allattentionhere.autoplayvideos.AAH_VideoImage
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.GameGenericPlatform
import com.project.gamersgeek.models.platforms.GenericPlatformDetails
import com.project.gamersgeek.utils.GlideApp

class AllGameResultAdapter constructor(private val gameResultClickListener: GameResultClickListener,
                                       private val gamePlatformIconClickListener: PlatformIconAdapter.PlatformIconClickListener): PagedListAdapter<Results, AllGameResultAdapter.GameVideoViewHolder>(
    GAME_RESULT_COMPARATOR){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameVideoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_all_game_items_layout, parent, false)
        return GameVideoViewHolder(view, parent.context, this.gamePlatformIconClickListener)
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
        holder.tapToPlayAndPause()
        holder.getExpandVideoBt()?.setOnClickListener {
            val gameResults: Results = holder.itemView.tag as Results
            this.gameResultClickListener.onVideoClicked(gameResults, VideoItemClickType.EXPAND_VIDEO)
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

    inner class GameVideoViewHolder constructor(private val view: View, private val context: Context,
                                                private val gamePlatformIconClickListener: PlatformIconAdapter.PlatformIconClickListener): AAH_CustomViewHolder(view) {
        private var volumeBt: AppCompatImageView?= null
        private var playBackBt: AppCompatImageView?= null
        private var iconRecyclerView: RecyclerView?= null
        private var gameNameView: AppCompatTextView?= null
        private var expandVideoBt: AppCompatImageView?= null
        private var videoImageView: AAH_VideoImage?= null
        private var fadeInFadeOutAnim: Animation?= null
        var isMute: Boolean?= false
        private var iconAdapter: PlatformIconAdapter?= null

        init {
            this.volumeBt = this.view.findViewById(R.id.all_games_item_vol_bt_id)
            this.playBackBt = this.view.findViewById(R.id.all_game_items_playback_bt_id)
            this.iconRecyclerView = this.view.findViewById(R.id.game_icon_list_view_id)
            this.expandVideoBt = this.view.findViewById(R.id.all_game_expand_video_bt_id)
            this.videoImageView = this.view.findViewById(R.id.all_game_video_view_id)
            this.iconRecyclerView?.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            this.iconAdapter = PlatformIconAdapter(this.gamePlatformIconClickListener)
            this.iconRecyclerView?.adapter = this.iconAdapter
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
            this.gameNameView?.let {nameView ->
               data.name.let {
                   nameView.text = it
               }
            }
            val platTypeList: MutableList<GenericPlatformDetails>? = arrayListOf()
            for (plat: GameGenericPlatform in data.genericPlatformList!!) {
                platTypeList?.add(plat.genericPlatformType)
            }
            platTypeList?.let {it ->
                if (it.isNotEmpty()) {
                    this.iconAdapter?.setData(it)
                }
            }
            isLooping = true
        }

        @SuppressLint("ClickableViewAccessibility")
        fun tapToPlayAndPause() = this.videoImageView?.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                isPaused = if (isPlaying) {
                    pauseVideo()
                    true
                } else {
                    playVideo()
                    false
                }
            }
            return@setOnTouchListener true
        }

        override fun pauseVideo() {
            super.pauseVideo()
            this.playBackBt?.visibility = View.VISIBLE
            this.expandVideoBt?.visibility = View.VISIBLE
            this.fadeInFadeOutAnim = AnimationUtils.loadAnimation(this.context, R.anim.anim_fade_in)
            this.playBackBt?.animation = fadeInFadeOutAnim
            this.expandVideoBt?.animation = fadeInFadeOutAnim
            this.playBackBt?.setImageResource(R.drawable.playicon)
        }

        override fun videoStarted() {
            super.videoStarted()
            this.playBackBt?.visibility = View.GONE
            this.expandVideoBt?.visibility = View.GONE
            this.fadeInFadeOutAnim = AnimationUtils.loadAnimation(this.context, R.anim.anim_fade_out)
            this.playBackBt?.animation = fadeInFadeOutAnim
            this.expandVideoBt?.animation = fadeInFadeOutAnim
        }

        fun getVolumeBt(): AppCompatImageView? {
            return this.volumeBt
        }
        fun getPlayBackBt(): AppCompatImageView? {
            return this.playBackBt
        }

        fun getGameNameView(): AppCompatTextView? {
            return this.gameNameView
        }

        fun getExpandVideoBt(): AppCompatImageView? {
            return this.expandVideoBt
        }
    }

    private fun switchVolumeBt(isMute: Boolean, volumeBt: AppCompatImageView) {
        if (isMute) {
            volumeBt.setImageResource(R.drawable.muteicon)
        } else {
            volumeBt.setImageResource(R.drawable.unmuteicon)
        }
    }

    interface GameResultClickListener {
        fun onVideoClicked(results: Results, type: VideoItemClickType)
    }

    enum class VideoItemClickType {
        EXPAND_VIDEO
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