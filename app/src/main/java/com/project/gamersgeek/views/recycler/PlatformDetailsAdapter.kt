package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.PlatformDetails

class PlatformDetailsAdapter: PagedListAdapter<PlatformDetails, RecyclerView.ViewHolder>(
    PLATFORM_DETAILS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_platform_item_layout, parent, false)
        return PlatformDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val platformDetails: PlatformDetails = getItem(position)!!
        platformDetails.let {
            (holder as PlatformDetailsViewHolder).bindView(it)
        }
    }

    inner class PlatformDetailsViewHolder constructor(private val view: View): RecyclerView.ViewHolder(view) {
        private var platformImageView: AppCompatImageView?= null

        init {
            this.platformImageView = this.view.findViewById(R.id.platform_image_view)
        }

        fun bindView(data: PlatformDetails) {
            this.itemView.tag = data
            val imageUrl: String = data.imageBackground
            Glide.with(this.view).load(imageUrl).into(this.platformImageView!!)
        }
    }


    companion object {
        private val PLATFORM_DETAILS_COMPARATOR = object : DiffUtil.ItemCallback<PlatformDetails>() {
            override fun areItemsTheSame(
                oldItem: PlatformDetails,
                newItem: PlatformDetails
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PlatformDetails,
                newItem: PlatformDetails
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}