package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.RawDescriptions
import com.project.gamersgeek.views.recycler.items.ScreenShots
import uk.co.deanwild.flowtextview.FlowTextView
import java.lang.IllegalArgumentException

class GameDetailsItemAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
       return when (viewType) {
           FLO_TEXT_VIEW -> {
               val rawDescriptionView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_raw_desc_items, parent, false)
               RawDescViewHolder(parent.context, rawDescriptionView)
           }
           SCREEN_SHOTS -> {
               val screenShotView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_screenshots_recyclerview, parent, false)
               ScreenShotViewHolder(parent.context, screenShotView)
           }
           else -> throw IllegalArgumentException("Unsupported view")
       }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val items: Any = this.data[position]
        when (holder) {
            is RawDescViewHolder -> holder.bindView(items as RawDescriptions)
            is ScreenShotViewHolder -> holder.bindView(items as ScreenShots)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is RawDescriptions -> FLO_TEXT_VIEW
            is ScreenShots -> SCREEN_SHOTS
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }

    fun setGameDetailsData(list: MutableList<Any>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class RawDescViewHolder(private val context: Context, private val view: View): BaseViewHolder<RawDescriptions>(view) {
        private var floatTextView: FlowTextView?= null
        private var bgImageView: AppCompatImageView?= null

        init {
            this.floatTextView = this.view.findViewById(R.id.game_details_raw_desc_view_id)
            this.bgImageView = this.view.findViewById(R.id.game_details_raw_des_inner_image_view_id)
        }
        override fun bindView(item: RawDescriptions) {
           itemView.tag = item
            item.let {desc ->
                this.floatTextView?.let {
                    it.textColor = ContextCompat.getColor(this.context, R.color.gray_500)
                    it.setTextSize(32f)
                    it.text = desc.description
                }
                val circularProgressDrawable = CircularProgressDrawable(this.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.setColorSchemeColors(Color.GRAY)
                circularProgressDrawable.start()
                this.bgImageView?.let {

                    GlideApp.with(this.view).load(desc.additionalImage)
                        .placeholder(circularProgressDrawable)
                        .into(it)
                }
            }
        }
    }
    inner class ScreenShotViewHolder(private val context: Context, private val view: View): BaseViewHolder<ScreenShots>(view) {
        private var screenShotRecyclerView: RecyclerView?= null
        private var screenshotAdapter: ScreenShotAdapter? = null
        init {
            this.screenShotRecyclerView = this.view.findViewById(R.id.game_details_screenshot_recycler_view_id)
            this.screenShotRecyclerView?.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            this.screenshotAdapter = ScreenShotAdapter()
            this.screenShotRecyclerView?.adapter = this.screenshotAdapter
        }

        override fun bindView(item: ScreenShots) {
           item.screenShotList?.let {
               this.screenshotAdapter?.setData(it)
           }
        }
    }

    companion object {
        private const val FLO_TEXT_VIEW = 0
        private const val SCREEN_SHOTS = 1
        private const val DEVELOPERS_GENRES = 2
        private const val PC_REQUIREMENTS = 3
        private const val FOOTER = 4
    }
}