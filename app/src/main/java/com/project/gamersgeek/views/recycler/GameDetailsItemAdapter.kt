package com.project.gamersgeek.views.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.views.recycler.items.RawDescriptions

class GameDetailsItemAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<Any> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class RawDescViewHolder(private val view: View): BaseViewHolder<RawDescriptions>(view) {

        override fun bindView(item: RawDescriptions) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}