package com.project.gamersgeek.views.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.project.gamersgeek.R
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.*
import uk.co.deanwild.flowtextview.FlowTextView


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
           DEVELOPERS_GENRES -> {
               val devAndGenView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_dev_gernres_main_layout, parent, false)
               DevAndGenresViewHolder(parent.context, devAndGenView)
           }
           PC_REQUIREMENTS -> {
               val pcRequirementView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_pc_requirements_layout, parent, false)
               PcRequirementViewHolder(pcRequirementView)
           }
           FOOTER -> {
               val footerView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_footer_layout, parent, false)
               FooterViewHolder(footerView)
           }
           else -> throw IllegalArgumentException("Unsupported view")
       }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val items: Any = this.data[position]
        when (holder) {
            is RawDescViewHolder -> holder.bindView(items as RawDescriptions)
            is ScreenShotViewHolder -> holder.bindView(items as ScreenShots)
            is DevAndGenresViewHolder -> holder.bindView(items as GameDevAndGenres)
            is PcRequirementViewHolder -> holder.bindView(items as PcRequirements)
            is FooterViewHolder -> holder.bindView(items as GameDetailsFooter)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is RawDescriptions -> FLO_TEXT_VIEW
            is ScreenShots -> SCREEN_SHOTS
            is GameDevAndGenres -> DEVELOPERS_GENRES
            is PcRequirements -> PC_REQUIREMENTS
            is GameDetailsFooter -> FOOTER
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

    inner class DevAndGenresViewHolder(private val context: Context, private val view: View): BaseViewHolder<GameDevAndGenres>(view) {
        private var devRecyclerView: RecyclerView?= null
        private var genreRecyclerView: RecyclerView?= null
        private var devListAdapter: DeveloperListAdapter?= null
        private var genListAdapter: GenreListAdapter?= null

        init {
            this.devRecyclerView = this.view.findViewById(R.id.game_details_developer_recyclerview_id)
            this.genreRecyclerView = this.view.findViewById(R.id.game_details_genres_recycler_view_id)
            this.devRecyclerView?.layoutManager = LinearLayoutManager(this.context)
            this.genreRecyclerView?.layoutManager = LinearLayoutManager(this.context)
            this.devListAdapter = DeveloperListAdapter()
            this.genListAdapter = GenreListAdapter()
            this.devRecyclerView?.adapter = this.devListAdapter
            this.genreRecyclerView?.adapter = this.genListAdapter
        }
        override fun bindView(item: GameDevAndGenres) {
            item.devList?.let {devList ->
                this.devListAdapter?.setData(devList)
            }
            item.genresList?.let { genList ->
                this.genListAdapter?.setData(genList)
            }
        }
    }

    inner class PcRequirementViewHolder(private val view: View): BaseViewHolder<PcRequirements>(view) {
        private var titleView: AppCompatTextView?= null
        private var minimumReqView: AppCompatTextView?= null
        private var recommendedView: AppCompatTextView?= null

        init {
            this.titleView = view.findViewById(R.id.game_details_pc_requirements_title_view_id)
            this.minimumReqView = this.view.findViewById(R.id.game_details_pc_requirements_minimum_view_id)
            this.recommendedView = this.view.findViewById(R.id.game_details_pc_requirement_recommended_view_id)
        }
        override fun bindView(item: PcRequirements) {
            for (platformList: CategorizedGamePlatforms in item.platformList) {
                platformList.requirementsInEnglish?.let {req ->
                    this.titleView?.let {
                        it.visibility = View.VISIBLE
                    }
                    val minimumReq = "${Constants.fromHtml(req.minimium)}"
                    val recommendedReq = "${Constants.fromHtml(req.recommended)}"
                    val minimumSentenceFirstWord: String = Constants.getFirstWord(minimumReq)
                    val recommendedSenFirstWord: String = Constants.getFirstWord(recommendedReq)
                    val minimumFirstWordEndIndex: Int = minimumSentenceFirstWord.length
                    val recommendedFirstWordEndIndex: Int = recommendedSenFirstWord.length
                    this.minimumReqView?.let {
                        it.visibility = View.VISIBLE
                        Constants.boldFirstWord(minimumFirstWordEndIndex, minimumReq, it)
                    }
                    this.recommendedView?.let {
                        it.visibility = View.VISIBLE
                        Constants.boldFirstWord(recommendedFirstWordEndIndex, recommendedReq, it)
                    }
                }
            }
        }
    }

    inner class FooterViewHolder(private val view: View): BaseViewHolder<GameDetailsFooter>(view) {
        private var websiteLinkView: AppCompatTextView?= null
        private var esrbRatingView: AppCompatImageView?= null
        private var saveGameBt: AppCompatImageView?= null

        init {
            this.websiteLinkView = this.view.findViewById(R.id.game_details_footer_website_link_view_id)
            this.esrbRatingView = this.view.findViewById(R.id.game_details_footer_esrb_rating_view_id)
            this.saveGameBt = this.view.findViewById(R.id.game_details_footer_save_game_bt_id)
        }
        override fun bindView(item: GameDetailsFooter) {
           this.websiteLinkView?.let { v ->
               item.websiteUrl?.let {url ->
                   v.text = url
               }
           }
            this.esrbRatingView?.let { v ->
                item.esrbRating?.let { rating ->
                    when (rating) {
                        "Teen" -> {

                        }
                    }
                }
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