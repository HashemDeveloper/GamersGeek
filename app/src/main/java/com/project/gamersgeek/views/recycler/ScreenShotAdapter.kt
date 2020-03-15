package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.ShortScreenShot
import com.project.gamersgeek.utils.GlideApp

class ScreenShotAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val mutableList: MutableList<ShortScreenShot> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_screenshots_items, parent, false)
        return ItemViewHolder(parent.context, itemView)
    }

    override fun getItemCount(): Int {
        return this.mutableList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder) as ItemViewHolder).bindView(this.mutableList[position])
    }

    fun setData(list: List<ShortScreenShot>) {
        this.mutableList.clear()
        this.mutableList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val context: Context, private val view: View): BaseViewHolder<ShortScreenShot>(view) {
        private var screenShotImageView: AppCompatImageView?= null

        init {
            this.screenShotImageView = this.view.findViewById(R.id.game_details_screenshot_view_id)
        }
        override fun bindView(item: ShortScreenShot) {
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.GRAY)
            circularProgressDrawable.start()
            this.screenShotImageView?.let {
                GlideApp.with(this.view).load(item.image)
                    .placeholder(circularProgressDrawable)
                    .into(it)
            }
        }
    }
}