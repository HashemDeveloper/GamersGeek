package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformGames
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.GameListWrapper
import uk.co.deanwild.flowtextview.FlowTextView

class PlatformDetailsAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val data: MutableList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            PLATFORM_INFO -> {
                val infoView: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_platform_detail_item_layout, parent, false)
                PlatformInfoHolder(infoView, parent.context)
            }
            PLATFORM_GAME_LIST -> {
                val gameListView: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_platform_detail_game_list_layout, parent, false)
                PlatformGameListHolder(gameListView, parent.context)
            }
            else -> throw IllegalArgumentException("Unsupported view")
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item: Any = this.data[position]
        when (holder) {
            is PlatformInfoHolder -> holder.bindView(item as PlatformDetails)
            is PlatformGameListHolder -> holder.bindView(item as GameListWrapper)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is PlatformDetails -> PLATFORM_INFO
            is GameListWrapper -> PLATFORM_GAME_LIST
            else -> throw IllegalArgumentException("Invalid index position $position")
        }
    }

    fun add(data: List<Any>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class PlatformInfoHolder constructor(private val view: View, private val context: Context): BaseViewHolder<PlatformDetails>(view) {
        private var descView: FlowTextView?= null
        private var imageView: AppCompatImageView?= null
        init {
            this.descView = this.view.findViewById(R.id.fragment_platform_detail_item_layout_description_view_id)
            this.imageView = this.view.findViewById(R.id.fragment_platform_detail_item_layout_image_view_id)
        }
        override fun bindView(item: PlatformDetails) {
            item.description?.let { d ->
                val desc: String = Constants.beautifyString(d)
                this.descView?.let { textView ->
                    textView.textColor = ContextCompat.getColor(this.context, R.color.gray_500)
                    textView.setTextSize(42f)
                    textView.text = desc
                }
            }
            val circularProgressDrawable: CircularProgressDrawable = Constants.glideCircularAnim(this.context)
            circularProgressDrawable.start()
            this.imageView?.let { i ->
                GlideApp.with(this.view).load(item.imageBackground)
                    .placeholder(circularProgressDrawable)
                    .into(i)
            }
        }
    }

    inner class PlatformGameListHolder constructor(private val view: View, private val context: Context): BaseViewHolder<GameListWrapper>(view) {
        private var platformGameListAdapter: PlatformGameListAdapter?= null
        private var headerView: MaterialTextView?= null
        private var listView: RecyclerView?= null
        init {
            this.headerView = this.view.findViewById(R.id.fragment_platform_detail_game_list_header_view_id)
            this.listView = this.view.findViewById(R.id.fragment_platform_details_game_list_recyclerview_id)
            this.listView?.layoutManager = LinearLayoutManager(this.context)
            this.platformGameListAdapter = PlatformGameListAdapter()
            this.listView?.adapter = this.platformGameListAdapter
        }
        override fun bindView(item: GameListWrapper) {
            this.headerView?.let { textView ->
                val headerTitle = "Some of the most popular games namely:"
                textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                textView.text = headerTitle
            }
            item.list?.let {
                this.platformGameListAdapter?.add(it)
            }
        }
    }

    companion object {
        private const val PLATFORM_INFO: Int = 0
        private const val PLATFORM_GAME_LIST: Int = 1
    }
}