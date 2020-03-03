package com.project.gamersgeek.views.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.GenericPlatformDetails

class PlatformIconAdapter : RecyclerView.Adapter<BaseViewHolder<*>>(){
    private var iconData: MutableList<GenericPlatformDetails> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val iconView: View = LayoutInflater.from(parent.context).inflate(R.layout.platform_icons_item, parent, false)
        return PlatformIconViewHolder(iconView)
    }

    override fun getItemCount(): Int {
       return this.iconData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as PlatformIconViewHolder).bindView(this.iconData[position])
    }


    fun setData(data: MutableList<GenericPlatformDetails>) {
        this.iconData.clear()
        this.iconData.addAll(data)
        notifyDataSetChanged()
    }

    private inner class PlatformIconViewHolder(val view: View): BaseViewHolder<GenericPlatformDetails>(view) {
        private var iconView: AppCompatImageView?= null

        init {
            this.iconView = this.view.findViewById(R.id.platform_icon_view_id)
        }

        override fun bindView(item: GenericPlatformDetails) {
            this.itemView.tag = item
            this.iconView?.let {
                var platformType: String
                item.let {
                   platformType = it.slug
                }
                if ("" != platformType) {
                    when (platformType) {
                        "pc" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.microsoft_windows_icon)
                        }
                        "playstation" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.play_station_icon)
                        }
                        "xbox" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.xbox_icon)
                        }
                        "nintendo" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.nintendo_icon)
                        }
                        "mac" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.mac_icon)
                        }
                        "android" -> {
                            this.iconView?.visibility = View.VISIBLE
                            this.iconView?.setImageResource(R.drawable.android_icon)
                        }
                        else -> {
                            this.iconView?.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}