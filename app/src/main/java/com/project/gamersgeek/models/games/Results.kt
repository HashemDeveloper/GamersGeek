package com.project.gamersgeek.models.games

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import com.project.gamersgeek.models.platforms.GameGenericPlatform
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.jetbrains.annotations.NotNull
import javax.annotation.Nullable

@Entity(tableName = "game_results")
@Parcelize
data class Results(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int,
    @ColumnInfo(name = "slug")
    @SerializedName("slug")
    @Expose
    var slug: String,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    var name: String,
    @ColumnInfo(name = "released")
    @SerializedName("released")
    @Expose
    var released: String,
    @ColumnInfo(name = "tba")
    @SerializedName("tba")
    @Expose
    var tba: Boolean,
    @ColumnInfo(name = "background_image")
    @SerializedName("background_image")
    @Expose
    var backgroundImage: String,
    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    @Expose
    var rating: Double,
    @ColumnInfo(name = "rating_top")
    @SerializedName("rating_top")
    @Expose
    var ratingTop: Int,
    @ColumnInfo(name = "ratings")
    @SerializedName("ratings")
    @Expose
    var ratingList: List<Rating>?= arrayListOf(),
    @ColumnInfo(name = "ratings_count")
    @SerializedName("ratings_count")
    @Expose
    var ratingCount: Int,
    @ColumnInfo(name = "reviews_text_count")
    @SerializedName("reviews_text_count")
    @Expose
    var reviewsTextCount: Int,
    @ColumnInfo(name = "added")
    @SerializedName("added")
    @Expose
    var added: Int,
    @ColumnInfo(name = "added_by_status")
    @SerializedName("added_by_status")
    @Expose
    var addedByStatus: @RawValue AddedByStatus,
    @ColumnInfo(name = "metacritic")
    @SerializedName("metacritic")
    @Expose
    var metaCritic: Int,
    @ColumnInfo(name = "playtime")
    @SerializedName("playtime")
    @Expose
    var playTime: Long,
    @ColumnInfo(name = "suggestions_count")
    @SerializedName("suggestions_count")
    @Expose
    var suggestionsCount: Int,
    @ColumnInfo(name = "reviews_count")
    @SerializedName("reviews_count")
    @Expose
    var reviewsCount: Int,
    @ColumnInfo(name = "saturated_color")
    @SerializedName("saturated_color")
    @Expose
    var saturatedColor: String,
    @ColumnInfo(name = "dominant_color")
    @SerializedName("dominant_color")
    @Expose
    var dominentColor: String,
    @ColumnInfo(name = "platforms")
    @SerializedName("platforms")
    @Expose
    var platformList: List<CategorizedGamePlatforms>?= arrayListOf(),
    @ColumnInfo(name = "parent_platforms")
    @SerializedName("parent_platforms")
    @Expose
    var genericPlatformList: List<GameGenericPlatform>?= arrayListOf(),
    @ColumnInfo(name = "genres")
    @SerializedName("genres")
    @Expose
    var genresList: List<GameGenre>?= arrayListOf(),
    @ColumnInfo(name = "stores")
    @SerializedName("stores")
    @Expose
    var listOfStores: List<Store>?= arrayListOf(),
    @ColumnInfo(name = "clip")
    @SerializedName("clip")
    @Expose
    var videoClip: @RawValue VideoClip?,
    @ColumnInfo(name = "short_screenshots")
    @SerializedName("short_screenshots")
    @Expose
    var shortScreenShotList: List<ShortScreenShot>?= arrayListOf()
): Parcelable