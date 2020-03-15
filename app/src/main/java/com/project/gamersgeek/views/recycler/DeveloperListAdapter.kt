package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.developer.Developer

class DeveloperListAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<Developer> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_developer_lists_items, parent, false)
        return DeveloperListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as DeveloperListViewHolder).bindView(this.data[position])
    }

    fun setData(devList: List<Developer>) {
        this.data.clear()
        this.data.addAll(devList)
        notifyDataSetChanged()
    }
    inner class DeveloperListViewHolder(private val view: View): BaseViewHolder<Developer>(view) {
        private var devNameView: AppCompatTextView?= null

        init {
            this.devNameView = this.view.findViewById(R.id.game_details_developer_name_view_id)
        }
        override fun bindView(item: Developer) {
            this.devNameView?.let {
                it.text = item.name
            }
        }
    }
}