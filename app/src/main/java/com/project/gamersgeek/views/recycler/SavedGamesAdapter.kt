package com.project.gamersgeek.views.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SavedGamesAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        TODO("Not yet implemented")
    }

    enum class SectionType {
        GAME_PLAYED,
        WISH_TO_PLAY,
    }
}