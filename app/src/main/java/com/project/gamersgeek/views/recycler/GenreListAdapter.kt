package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.GameGenre

class GenreListAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<GameGenre> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_genres_list_items, parent, false)
        return GenreListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as GenreListViewHolder).bindView(this.data[position])
    }

    fun setData(list: List<GameGenre>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }
    inner class GenreListViewHolder(private val view: View): BaseViewHolder<GameGenre>(view) {
        private var genreNameView: AppCompatTextView?= null

        init {
            this.genreNameView = this.view.findViewById(R.id.game_details_genres_name_view_id)
        }

        override fun bindView(item: GameGenre) {
            this.genreNameView?.let {
                it.text = item.name
            }
        }
    }
}