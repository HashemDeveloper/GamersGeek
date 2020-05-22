package com.project.gamersgeek.views.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.models.platforms.PlatformGames

class PlatformGameListAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<PlatformGames> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
       return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as GameListHolder).bindView(this.data[position])
    }
    inner class GameListHolder constructor(private val view: View): BaseViewHolder<PlatformGames>(view) {

        override fun bindView(item: PlatformGames) {

        }
    }
}