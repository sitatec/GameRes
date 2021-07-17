package dev.berete.gameres.data_sources.remote

import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.imageBuilder
import com.neovisionaries.i18n.CountryCode
import dev.berete.gameres.domain.models.*
import dev.berete.gameres.domain.models.enums.*
import dev.berete.gameres.domain.models.enums.AgeRating
import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar
import proto.*
import proto.ReleaseDate as ReleaseDateDTO
import proto.GameMode as GameModeDTO
import proto.Game as GameDTO
import proto.Website as WebsiteDTO
import proto.InvolvedCompany as GameCompanyDTO
import proto.PlayerPerspective as PlayerPerspectiveDTO
import proto.Platform as PlatformDTO
import java.lang.IllegalArgumentException
import java.util.*

// ------------- Data Transfer Object to Domain Models Converters --------------- //

/**
 * Converts this Game DTO (Data Transfer Object) instance to the domain's [Game] model
 */
fun GameDTO.toDomainGame(
    largeImageSize: ImageSize = ImageSize.SCREENSHOT_MEDIUM,
    coverSize: ImageSize = ImageSize.COVER_BIG,
) = Game(
    id = id,
    name = name,
    genres = genresList.map(Genre::toDomainGameGenre),
    platformList = platformsList.map(PlatformDTO::toDomainGamePlatform).toList(),
    firstReleaseDate = Date(firstReleaseDate.seconds * 1000),
    releases = releaseDatesList.map(ReleaseDateDTO::toDomainRelease),
    summary = summary,
    storyline = storyline,
    coverUrl = imageBuilder(cover.imageId, coverSize),
    artWorkUrls = artworksList.map { imageBuilder(it.imageId, largeImageSize) },
    screenshotUrls = screenshotsList.map { imageBuilder(it.imageId, largeImageSize) },
    videoList = videosList.map(GameVideo::toDomainVideo),
    similarGameIds = similarGamesList.map { it.id },
    rating = totalRating,
    ratingCount = totalRatingCount,
    ageRatings = ageRatingsList.map {
        if (it.rating == AgeRatingRatingEnum.AGERATING_RATING_NULL) {
            AgeRating.UNRECOGNIZED
        } else {
            AgeRating.valueOf(it.rating.name)
        }
    },
    gameModes = gameModesList.map(GameModeDTO::toDomainGameMode),
    playerPerspectives = playerPerspectivesList.map(PlayerPerspectiveDTO::toDomainPlayerPerspective),
    developers = involvedCompaniesList.filter { it.developer }
        .map(GameCompanyDTO::toDomainGameCompany),
    publishers = involvedCompaniesList.filter { it.publisher }
        .map(GameCompanyDTO::toDomainGameCompany),
    websiteList = websitesList
        .filter {
            it.category != WebsiteCategoryEnum.WEBSITE_CATEGORY_NULL && it.category != WebsiteCategoryEnum.UNRECOGNIZED
        }
        .map(WebsiteDTO::toDomainWebsite),
)

/**
 * Converts this Game Genre DTO (Data Transfer Object) instance to the domain's [GameGenre]
 */
fun Genre.toDomainGameGenre(): GameGenre {
    return try {
        GameGenre.valueOf(name.uppercase())
    } catch (e: IllegalArgumentException) {
        GameGenre.OTHER
    }
}

/**
 * Converts this Game Platform DTO (Data Transfer Object) instance to the domain's [PlatformType]
 */
fun PlatformDTO.toDomainGamePlatform(): Platform {
    val platformName = name.lowercase()
    val platformType = when {
        platformName.contains("playstation") -> PlatformType.PLAYSTATION
        platformName.contains("xbox") -> PlatformType.XBOX
        platformName.contains("microsoft windows") -> PlatformType.WINDOWS
        platformName.contains("wii") -> PlatformType.WII
        platformName == "nintendo switch" -> PlatformType.NINTENDO_SWITCH
        platformName == "android" -> PlatformType.ANDROID
        platformName == "mac" || platformName == "ios" -> PlatformType.APPLE
        platformName == "linux" -> PlatformType.LINUX
        else -> PlatformType.OTHER
    }
    return Platform(
        platformType = platformType,
        name = if (platformName.contains("microsoft windows")) "PC Windows" else name
    )
}

/**
 * Converts this Game Mode DTO (Data Transfer Object) instance to the domain's [GameMode]
 */
fun GameModeDTO.toDomainGameMode(): GameMode {
    return when (name) {
        "Single player" -> GameMode.SINGLE_PLAYER
        "Multiplayer" -> GameMode.MULTIPLAYER
        "Co-operative" -> GameMode.COOPERATIVE
        "Split screen" -> GameMode.SPLIT_SCREEN
        "Massively Multiplayer Online (MMO)" -> GameMode.MMO
        "Battle Royale" -> GameMode.BATTLE_ROYALE
        else -> GameMode.OTHER
    }
}

/**
 * Converts this Player Perspective DTO (Data Transfer Object) instance to the domain's [PlayerPerspective]
 */
fun PlayerPerspectiveDTO.toDomainPlayerPerspective(): PlayerPerspective {
    return when (name) {
        "First person" -> PlayerPerspective.FIRST_PERSON
        "Third person" -> PlayerPerspective.THIRD_PERSON
        "Auditory" -> PlayerPerspective.AUDITORY
        "Bird view / Isometric" -> PlayerPerspective.BIRD_VIEW
        "Side view" -> PlayerPerspective.SIDE_VIEW
        "Text" -> PlayerPerspective.TEXT
        "Virtual Reality" -> PlayerPerspective.VIRTUAL_REALITY
        else -> PlayerPerspective.OTHER
    }
}

/**
 * Converts this Game Company DTO (Data Transfer Object) instance to the domain's [GameCompany]
 */
fun GameCompanyDTO.toDomainGameCompany(): GameCompany {
    return GameCompany(
        id = company.id,
        name = company.name,
        description = company.description,
        country = CountryCode.getByCode(company.country)?.getName() ?: "",
        logoUrl = if (company.hasLogo()) imageBuilder(company.logo.imageId, ImageSize.LOGO_MEDIUM)
        else ""
    )
}

/**
 * Converts this Video DTO to domain video
 */
fun GameVideo.toDomainVideo() = Video(
    url = "https://www.youtube.com/watch?v=${videoId}",
    title = name,
    thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg",
)

fun WebsiteDTO.toDomainWebsite() = when (category!!) {
    WebsiteCategoryEnum.WEBSITE_OFFICIAL -> Website(
        url,
        "Official Website",
        "file:///android_asset/official_website_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_WIKIA -> Website(
        url,
        "Fandom (Wikia)",
        "file:///android_asset/wikia_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_WIKIPEDIA -> Website(
        url,
        "Wikipedia",
        "file:///android_asset/wikipedia_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_FACEBOOK -> Website(
        url,
        "Facebook",
        "file:///android_asset/facebook_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_TWITTER -> Website(
        url,
        "Twitter",
        "file:///android_asset/twitter_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_TWITCH -> Website(
        url,
        "Twitch",
        "file:///android_asset/twitch_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_INSTAGRAM -> Website(
        url,
        "Instagram",
        "file:///android_asset/instagram_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_YOUTUBE -> Website(
        url,
        "Youtube",
        "file:///android_asset/youtube_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_IPHONE -> Website(
        url,
        "App Store (Iphone)",
        "file:///android_asset/appstore_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_IPAD -> Website(
        url,
        "App store (Ipad)",
        "file:///android_asset/appstore_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_ANDROID -> Website(
        url,
        "Google Play Store",
        "file:///android_asset/play_store_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_STEAM -> Website(
        url,
        "Steam",
        "file:///android_asset/steam_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_REDDIT -> Website(
        url,
        "Reddit",
        "file:///android_asset/reddit_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_ITCH -> Website(
        url,
        "Itch",
        "file:///android_asset/itch_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_EPICGAMES -> Website(
        url,
        "Epic Games",
        "file:///android_asset/epic_games_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_GOG -> Website(url, "Gog", "file:///android_asset/gog_logo.png")
    WebsiteCategoryEnum.WEBSITE_DISCORD -> Website(
        url,
        "Discord",
        "file:///android_asset/discord_logo.png",
    )
    WebsiteCategoryEnum.UNRECOGNIZED -> throw IllegalStateException()
    WebsiteCategoryEnum.WEBSITE_CATEGORY_NULL -> throw IllegalStateException()
}

fun ReleaseDateDTO.toDomainRelease() = Release(
    date = Date(date.seconds * 1000),
    platform = platform.toDomainGamePlatform(),
    region = region.name.toLowercaseExceptFirstChar().replace("_", " "),
    gameId = game.id,
    gameName = game.name,
    gameCoverUrl = imageBuilder(game.cover.imageId, ImageSize.COVER_BIG),
    artWorkUrl = if (game.artworksList.isEmpty()) ""
    else imageBuilder(
        game.artworksList.first().imageId,
        ImageSize.SCREENSHOT_MEDIUM,
    )
)

// ------------- Domain Models to Data Transfer Object Converters --------------- //


//fun GameGenre.toDTOName(): String {
//    return when(this){
//        GameGenre.RPG -> "Role-playing (RPG)"
//        else -> toString()
//    }
//}