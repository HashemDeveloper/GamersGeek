package com.project.gamersgeek.views.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Results
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import com.project.gamersgeek.utils.Constants
import com.project.gamersgeek.utils.EsrbRatingType
import com.project.gamersgeek.utils.GlideApp
import com.project.gamersgeek.views.recycler.items.*
import com.stfalcon.frescoimageviewer.ImageViewer
import uk.co.deanwild.flowtextview.FlowTextView


class GameDetailsItemAdapter(
    private val gameDetailsClickListener: GameDetailsClickListener,
    private val gameDetails: Results
): RecyclerView.Adapter<BaseViewHolder<*>>() {
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
           WHERE_TO_BUY -> {
               val whereToBuyView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_where_to_buy_layout, parent, false)
               WhereToBuyViewHolder(parent.context, whereToBuyView)
           }
           FOOTER -> {
               val footerView: View = LayoutInflater.from(parent.context).inflate(R.layout.game_details_footer_layout, parent, false)
               val footerViewHolder = FooterViewHolder(footerView)
               footerViewHolder.getSaveGameBt()?.let { bt ->
                   bt.setOnClickListener {
                       val gameDetailsFooter: GameDetailsFooter = footerViewHolder.itemView.tag as GameDetailsFooter
                       gameDetailsFooter.gameResult = this.gameDetails
                       this.gameDetailsClickListener.onGameDetailsItemClicked(gameDetailsFooter)
                   }
               }
               footerViewHolder
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
            is WhereToBuyViewHolder -> holder.bindView(items as GameDetailsStores)
            is FooterViewHolder -> holder.bindView(items as GameDetailsFooter)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is RawDescriptions -> FLO_TEXT_VIEW
            is ScreenShots -> SCREEN_SHOTS
            is GameDevAndGenres -> DEVELOPERS_GENRES
            is PcRequirements -> PC_REQUIREMENTS
            is GameDetailsStores -> WHERE_TO_BUY
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
                val circularProgressDrawable: CircularProgressDrawable = Constants.glideCircularAnim(this.context)
                circularProgressDrawable.start()
                this.bgImageView?.let {

                    GlideApp.with(this.view).load(desc.additionalImage)
                        .placeholder(circularProgressDrawable)
                        .into(it)
                }
                val hierarchyBuilder: GenericDraweeHierarchyBuilder =
                    GenericDraweeHierarchyBuilder.newInstance(this.context.resources)
                        .setProgressBarImage(circularProgressDrawable)
                val imageList: Array<String> = arrayOf(desc.additionalImage)
                val enlargeImage: ImageViewer.Builder<String> = ImageViewer.Builder<String>(this.context, imageList)
                this.bgImageView?.setOnClickListener {
                    enlargeImage
                        .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                        .allowZooming(true)
                        .allowSwipeToDismiss(true)
                        .show()
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

    fun storeClickListener() = object: StoreListAdapter.StoreListClickListener {
        override fun onStoreClicked(url: String) {
            gameDetailsClickListener.onGameDetailsItemClicked(url)
        }
    }

    inner class WhereToBuyViewHolder(private val context: Context, private val view: View): BaseViewHolder<GameDetailsStores>(view) {
        private var storeListRecyclerView: RecyclerView?= null
        private var storeListAdapter: StoreListAdapter?= null

        init {
          this.storeListRecyclerView = this.view.findViewById(R.id.game_details_where_to_buy_recycler_view_id)
          this.storeListRecyclerView?.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
          this.storeListAdapter = StoreListAdapter(storeClickListener())
          this.storeListRecyclerView?.adapter = this.storeListAdapter
        }

        override fun bindView(item: GameDetailsStores) {
            this.storeListAdapter?.setupStoreList(item.storeList)
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
           this.itemView.tag = item
           this.websiteLinkView?.let { v ->
               item.websiteUrl?.let {url ->
                   v.text = url
               }
           }
            this.esrbRatingView?.let { v ->
                item.esrbRating?.let { rating ->
                    when (rating) {
                        EsrbRatingType.EVERYONE_PLUS_10.getType() -> {
                           v.setImageResource(R.drawable.rated_over_ten_esrbpng)
                        }
                        EsrbRatingType.EVERYONE.getType() -> {
                           v.setImageResource(R.drawable.rated_every_one)
                        }
                        EsrbRatingType.TEEN.getType() -> {
                           v.setImageResource(R.drawable.rated_teen_esrb)
                        }
                        EsrbRatingType.MATURE.getType() -> {
                            v.setImageResource(R.drawable.rated_mature_esrb)
                        }
                        EsrbRatingType.ADULTS_ONLY.getType() -> {
                           v.setImageResource(R.drawable.rated_adult_only_esrbpng)
                        } else -> ""
                    }
                }
            }
        }

        fun getSaveGameBt(): AppCompatImageView? {
            return this.saveGameBt
        }
    }

    interface GameDetailsClickListener {
        fun <T> onGameDetailsItemClicked(items: T)
    }

    companion object {
        private const val FLO_TEXT_VIEW = 0
        private const val SCREEN_SHOTS = 1
        private const val DEVELOPERS_GENRES = 2
        private const val PC_REQUIREMENTS = 3
        private const val WHERE_TO_BUY = 4
        private const val FOOTER = 5
    }
}