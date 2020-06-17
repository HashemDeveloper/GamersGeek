package com.project.gamersgeek.views.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.project.gamersgeek.R
import com.project.gamersgeek.models.creators.CreatorPosition
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp
import java.lang.StringBuilder

class CreatorPageAdapter constructor(private val listener: CreatorPageListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var data: MutableList<CreatorResults> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_creators_page_list_item, parent, false)
        val viewHolder = CreatorPageViewHolder(view, parent.context)
        viewHolder.getCreatorInfoCard()?.setOnClickListener { onClick ->
            val creatorResults: CreatorResults = viewHolder.itemView.tag as CreatorResults
            this.listener.onCardClicked(creatorResults)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
       return this.data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        ((holder as CreatorPageViewHolder).bindView(this.data[position]))
    }

    fun setData(list: List<CreatorResults>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }
    inner class CreatorPageViewHolder constructor(private val view: View, private val context: Context): BaseViewHolder<CreatorResults>(view) {
        private var creatorInfoCard: MaterialCardView?= null
        private var imageView: AppCompatImageView?= null
        private var nameView: MaterialTextView?= null
        private var positionView: MaterialTextView?= null


        init {
            this.creatorInfoCard = this.view.findViewById(R.id.fragment_creators_page_list_card_view_id)
            this.imageView = this.view.findViewById(R.id.fragment_creators_page_image_view_id)
            this.nameView = this.view.findViewById(R.id.fragment_creators_page_name_view_id)
            this.positionView = this.view.findViewById(R.id.fragment_creators_page_positions_view_id)
        }

        override fun bindView(item: CreatorResults) {
            this.itemView.tag = item
            val imageProgressBar: CircularProgressDrawable = Constants.glideCircularAnim(this.context)
            this.imageView?.let { view ->
                val imageUrl: String? = item.image_
                GlideApp.with(this.view).load(imageUrl)
                    .placeholder(imageProgressBar)
                    .into(view)
            }
            this.nameView?.let { view ->
                val name: String?= item.name_
                view.text = name
            }
            val positionListBuilder = StringBuilder()
            item.positionList?.let { posList ->
                for (pos: CreatorPosition in posList) {
                    positionListBuilder.append(pos.name_).append("|")
                }
            }
            val positionList: String = positionListBuilder.toString()
            val fixedPositionString: String?= Constants.removeLastUnwantedChar(positionList, '|')
            val capitalizeFinalString: String?= Constants.capitalizeFirstCharInWord(fixedPositionString, '|')
            this.positionView?.let { view ->
                view.text = capitalizeFinalString
            }
        }
        fun getCreatorInfoCard(): MaterialCardView? {
            return this.creatorInfoCard
        }
    }
    interface CreatorPageListener {
        fun onCardClicked(creatorInfo: CreatorResults)
    }
}