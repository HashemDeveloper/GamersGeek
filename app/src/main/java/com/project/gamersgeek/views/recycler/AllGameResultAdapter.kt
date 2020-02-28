package com.project.gamersgeek.views.recycler

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.project.gamersgeek.models.games.Results

class AllGameResultAdapter constructor(): PagedListAdapter<Results, AllGameResultAdapter.GameVideoViewHolder>(
    GAME_RESULT_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameVideoViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: GameVideoViewHolder, position: Int) {

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

    inner class GameVideoViewHolder constructor(itemView: View): AAH_CustomViewHolder(itemView) {

        override fun pauseVideo() {
            super.pauseVideo()
        }

        override fun videoStarted() {
            super.videoStarted()
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