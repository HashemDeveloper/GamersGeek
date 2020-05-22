package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.PlatformDetails
import com.project.gamersgeek.models.platforms.PlatformGames
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.utils.paging.NetworkState

class PlatformAdapter constructor(private val listener: PlatformListener): PagedListAdapter<PlatformDetails, RecyclerView.ViewHolder>(
    PLATFORM_DETAILS_COMPARATOR) {
    private var networkState: NetworkState?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_platform_item_layout, parent, false)
        val viewHolder = PlatformDetailsViewHolder(view, parent.context)
        viewHolder.getViewPlatformDetailsBt().setOnClickListener {
            val platformDetails: PlatformDetails = viewHolder.itemView.tag as PlatformDetails
            this.listener.onPlatformViewClicked(platformDetails)
        }
        viewHolder.getContainerView()?.setOnClickListener {
            val platformDetails: PlatformDetails = viewHolder.itemView.tag as PlatformDetails
            this.listener.onPlatformViewClicked(platformDetails)
        }
        viewHolder.getShowCaseGame1View().setOnClickListener {
            val platformDetails: PlatformDetails = viewHolder.itemView.tag as PlatformDetails
            platformDetails.games?.let {list ->
                val gameList: List<PlatformGames> = list.take(2)
                val gameId: Int = gameList[0].id
                this.listener.onShowGameClicked(gameId, ShowGameType.VIEW_GAME1)
            }
        }
        viewHolder.getShowCaseGame2View().setOnClickListener {
            val platformDetails: PlatformDetails = viewHolder.itemView.tag as PlatformDetails
            platformDetails.games?.let { list ->
                val gameList: List<PlatformGames> = list.take(2)
                val gameId: Int = gameList[1].id
                this.listener.onShowGameClicked(gameId, ShowGameType.VIEW_GAME2)
            }
        }
        return PlatformDetailsViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var platformDetails: PlatformDetails?
        getItem(position).let {
            platformDetails = it
        }
        platformDetails?.let {
            (holder as PlatformDetailsViewHolder).bindView(it)
        }
    }

    fun setNetworkState(it: NetworkState) {
//        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = it
        val hasExtraRow: Boolean = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow) {
            notifyItemChanged(itemCount - 1)
        }
    }
    private fun hasExtraRow() = this.networkState != null && this.networkState != NetworkState.LOADED

    inner class PlatformDetailsViewHolder constructor(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        private var container: ConstraintLayout?= null
        private var platformImageView: AppCompatImageView?= null
        private var viewPlatformDetails: AppCompatImageView?= null
        private var platformNameView: AppCompatTextView?= null
        private var gamesCountView: AppCompatTextView?= null
        private var showCaseGame1View: AppCompatTextView?= null
        private var showCaseGame2View: AppCompatTextView?= null
        private var gamesCountResult: AppCompatTextView?= null
        private var gamesOneAddedResult: AppCompatTextView?= null
        private var gamesTwoAddedResult: AppCompatTextView?= null

        init {
            this.container = this.view.findViewById(R.id.platform_game_container_id)
            this.platformImageView = this.view.findViewById(R.id.platform_image_view)
            this.viewPlatformDetails = this.view.findViewById(R.id.view_details_bt_id)
            this.platformNameView = this.view.findViewById(R.id.platform_name_view_id)
            this.gamesCountView = this.view.findViewById(R.id.platform_games_count_view_id)
            this.showCaseGame1View = this.view.findViewById(R.id.platform_show_case_game1)
            this.showCaseGame2View = this.view.findViewById(R.id.platform_show_case_game2)
            this.gamesCountResult = this.view.findViewById(R.id.platform_game_count_result_view_id)
            this.gamesOneAddedResult = this.view.findViewById(R.id.platform_game1_total_added_view_id)
            this.gamesTwoAddedResult = this.view.findViewById(R.id.platform_game2_total_added_view_id)
        }

        fun bindView(data: PlatformDetails) {
            this.itemView.tag = data
            this.platformNameView?.let {
                if (data.name != "Game Boy Advance") {
                    it.text = data.name
                }
            }
            val imageUrl: String = data.imageBackground
            val circularProgressDrawable: CircularProgressDrawable = Constants.glideCircularAnim(this.context)
            circularProgressDrawable.start()
            if (data.name != "Game Boy Advance") {
                GlideApp.with(this.view).load(imageUrl)
                    .roundedCorners(this.context, 8)
                    .placeholder(circularProgressDrawable)
                    .into(this.platformImageView!!)
                val popularGameCount: String = this.context.resources.getText(R.string.popular_games).toString()
                this.gamesCountView?.let {
                    it.text = popularGameCount
                }
                this.gamesCountResult?.let {
                    it.text = data.gamesCount.toString()
                }
                data.games?.let {list ->
                    val gameList: List<PlatformGames> = list.take(2)
                    val gameOne: String = gameList[0].name
                    val gameTwo: String = gameList[1].name
                    val gameOneAddedResult: Int = gameList[0].added
                    val gameTwoAddedResult: Int = gameList[1].added
                    this.showCaseGame1View?.let {
                        it.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                        it.text = gameOne
                    }
                    this.showCaseGame2View?.let {
                        it.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                        it.text = gameTwo
                    }
                    this.gamesOneAddedResult?.let {
                        it.text = gameOneAddedResult.toString()
                    }
                    this.gamesTwoAddedResult?.let {
                        it.text = gameTwoAddedResult.toString()
                    }
                }
            }
        }

        fun getViewPlatformDetailsBt(): AppCompatImageView {
            return this.viewPlatformDetails!!
        }

        fun getShowCaseGame1View(): AppCompatTextView {
            return this.showCaseGame1View!!
        }

        fun getShowCaseGame2View(): AppCompatTextView {
            return this.showCaseGame2View!!
        }

        fun getContainerView(): ConstraintLayout? {
            return this.container
        }
    }

    interface PlatformListener {
        fun onPlatformViewClicked(platformDetails: PlatformDetails)
        fun onShowGameClicked(gameId: Int, showGameType: ShowGameType)
    }

    enum class ShowGameType {
        VIEW_GAME1,
        VIEW_GAME2
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