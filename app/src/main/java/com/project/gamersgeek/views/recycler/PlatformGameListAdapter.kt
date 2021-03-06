package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.PlatformGames

class PlatformGameListAdapter(private val nightModeOn: Boolean) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<PlatformGames> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_platform_detail_game_list_item_layout, parent, false)
        return GameListHolder(view, parent.context, this.nightModeOn)
    }

    override fun getItemCount(): Int {
       return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as GameListHolder).bindView(this.data[position])
    }
    fun add(list: List<PlatformGames>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }
    inner class GameListHolder(private val view: View, private val context: Context, private val nightModeOn: Boolean): BaseViewHolder<PlatformGames>(view) {
        private var titleView: MaterialTextView?= null
        init {
            this.titleView = this.view.findViewById(R.id.fragment_platform_details_game_title_view_id)
        }
        override fun bindView(item: PlatformGames) {
            this.titleView?.let { title ->
                if (this.nightModeOn) {
                    title.setTextColor(ContextCompat.getColor(this.context, R.color.gray_500))
                } else {
                    title.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                }
                title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                title.text = item.name
            }
        }
    }
}