package com.project.gamersgeek.views.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.models.publishers.DevPubResult
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp

class DevPubPageAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<DevPubResult> = arrayListOf()

    fun setData(list: List<DevPubResult>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_publisher_list_item_layout, parent, false)
        return PublisherPageViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as PublisherPageViewHolder).bindView(this.data[position])
    }

    inner class PublisherPageViewHolder constructor(private val view: View, private val context: Context): BaseViewHolder<DevPubResult>(view) {
        private var imageView: AppCompatImageView?= null
        private var titleView: AppCompatTextView?= null

        init {
            this.imageView = this.view.findViewById(R.id.fragment_publisher_page_image_view_id)
            this.titleView = this.view.findViewById(R.id.fragment_publisher_page_title_view_id)
        }

        override fun bindView(item: DevPubResult) {
            this.titleView?.let { v ->
                v.text = item.name
            }
            val circularProgressDrawable: CircularProgressDrawable = Constants.glideCircularAnim(this.context)
            circularProgressDrawable.start()
            this.imageView?.let { iView ->
                GlideApp.with(this.view).load(item.imageBackground)
                    .placeholder(circularProgressDrawable)
                    .into(iView)
            }
        }
    }
}