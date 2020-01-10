package com.project.gamersgeek.models.games

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.gamersgeek.models.developer.Developer
import com.project.gamersgeek.models.platforms.CategorizedGamePlatforms
import com.project.gamersgeek.models.platforms.GameGenericPlatform
import com.project.gamersgeek.models.publishers.Publisher
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GamesRes(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("name_original")
    @Expose
    var nameOriginal: String,
    @SerializedName("description")
    @Expose
    var description: String,
    @SerializedName("metacritic")
    @Expose
    var metacritic: String,
    @SerializedName("released")
    @Expose
    var released: String,
    @SerializedName("tba")
    @Expose
    var tba: String,
    @SerializedName("updated")
    @Expose
    var updated: String,
    @SerializedName("background_image")
    @Expose
    var backgroundImage: String,
    @SerializedName("background_image_additional")
    @Expose
    var backgroundImageAdditional: String,
    @SerializedName("website")
    @Expose
    var website: String,
    @SerializedName("rating")
    @Expose
    var rating: Double,
    @SerializedName("rating_top")
    @Expose
    var ratingTop: Int,
    @SerializedName("ratings")
    @Expose
    var ratingList: @RawValue List<Rating>?= arrayListOf(),
    @SerializedName("added")
    @Expose
    var added: Int,
    @SerializedName("added_by_status")
    @Expose
    var addedByStatus: @RawValue AddedByStatus,
    @SerializedName("playtime")
    @Expose
    var playTime: Int,
    @SerializedName("screenshots_count")
    @Expose
    var screenShotsCount: Int,
    @SerializedName("movies_count")
    @Expose
    var moviesCount: Int,
    @SerializedName("creators_count")
    @Expose
    var creatorsCount: Int,
    @SerializedName("achievements_count")
    @Expose
    var achievementsCoun: Int,
    @SerializedName("parent_achievements_count")
    @Expose
    var parentAchievementsCount: Int,
    @SerializedName("reddit_url")
    @Expose
    var redditUrl: String,
    @SerializedName("reddit_name")
    @Expose
    var redditName: String,
    @SerializedName("reddit_description")
    @Expose
    var redditDesc: String,
    @SerializedName("reddit_logo")
    @Expose
    var redditLogo: String,
    @SerializedName("reddit_count")
    @Expose
    var redditCount: Int,
    @SerializedName("twitch_count")
    @Expose
    var twitchCount: Int,
    @SerializedName("youtube_count")
    @Expose
    var youTubeCount: Int,
    @SerializedName("reviews_text_count")
    @Expose
    var reviewsTextCount: Int,
    @SerializedName("ratings_count")
    @Expose
    var ratingCount: Int,
    @SerializedName("suggestions_count")
    @Expose
    var suggestionsCount: Int,
    @SerializedName("alternative_names")
    @Expose
    var alternativeNames: @RawValue List<String>?= arrayListOf(),
    @SerializedName("metacritic_url")
    @Expose
    var metaCriticUrl: String,
    @SerializedName("parents_count")
    @Expose
    var parentsCount: Int,
    @SerializedName("additions_count")
    @Expose
    var additionsCount: Int,
    @SerializedName("game_series_count")
    @Expose
    var gameSeriesCount: Int,
    @SerializedName("user_game")
    @Expose
    var userGame: String?= "",
    @SerializedName("reviews_count")
    @Expose
    var reviewsCount: Int,
    @SerializedName("saturated_color")
    @Expose
    var saturatedColor: String,
    @SerializedName("dominant_color")
    @Expose
    var dominantColor: String,
    @SerializedName("parent_platforms")
    @Expose
    var genericPlatForms: @RawValue List<GameGenericPlatform>?= arrayListOf(),
    @SerializedName("platforms")
    @Expose
    var categorizedPlatformList: @RawValue List<CategorizedGamePlatforms>?= arrayListOf(),
    @SerializedName("stores")
    @Expose
    var stores: @RawValue List<Store>?= arrayListOf(),
    @SerializedName("developers")
    @Expose
    var developers: @RawValue List<Developer>?= arrayListOf(),
    @SerializedName("genres")
    @Expose
    var genres: @RawValue List<GameGenre>?= arrayListOf(),
    @SerializedName("tags")
    @Expose
    var tags: @RawValue List<Tag>?= arrayListOf(),
    @SerializedName("publishers")
    @Expose
    var publishers: @RawValue List<Publisher>?= arrayListOf(),
    @SerializedName("esrb_rating")
    @Expose
    var esrbRating: @RawValue EsRbRating,
    @SerializedName("clip")
    @Expose
    var clip: @RawValue VideoClip,
    @SerializedName("description_raw")
    @Expose
    var descriptionRaw: String
): Parcelable