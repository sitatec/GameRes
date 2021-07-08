package dev.berete.gameres.data_sources.remote

import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.imageBuilder
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.GameCompany
import dev.berete.gameres.domain.models.enums.*
import proto.GameMode as GameModeDTO
import proto.Game as GameDTO
import proto.Genre
import proto.InvolvedCompany as GameCompanyDTO
import proto.PlayerPerspective as PlayerPerspectiveDTO
import proto.Platform as PlatformDTO
import java.lang.IllegalArgumentException
import java.util.*

// ------------- Data Transfer Object to Domain Models Converters --------------- //

/**
 * Converts this Game DTO (Data Transfer Object) instance to the domain's [Game] model
 */
fun GameDTO.toDomainGame(largeImageSize: ImageSize = ImageSize.SCREENSHOT_MEDIUM) = Game(
    id = id,
    name = name,
    genres = genresList.map(Genre::toDomainGameGenre),
    platformList = platformsList.map(PlatformDTO::toDomainGamePlatform).toSet()
        .toList(), // toSet() removes double elements
    firstReleaseDate = Date(firstReleaseDate.seconds),
    releaseDates = releaseDatesList.map { Date(it.date.seconds) },
    summary = summary,
    storyline = storyline,
    coverUrl = imageBuilder(cover.imageId, ImageSize.COVER_BIG),
    artWorkUrls = artworksList.map { imageBuilder(it.imageId, largeImageSize) },
    screenshotUrls = screenshotsList.map { imageBuilder(it.imageId, largeImageSize) },
    videoUrls = videosList.map { "https://www.youtube.com/watch?v=${it.videoId}" },
    similarGameIds = similarGamesList.map { it.id },
    rating = totalRating,
    ratingCount = totalRatingCount,
    ageRatings = ageRatingsList.map { AgeRating.valueOf(it.rating.name) },
    gameModes = gameModesList.map(GameModeDTO::toDomainGameMode),
    playerPerspectives = playerPerspectivesList.map(PlayerPerspectiveDTO::toDomainPlayerPerspective),
    developers = involvedCompaniesList.filter { it.developer }
        .map(GameCompanyDTO::toDomainGameCompany),
    publishers = involvedCompaniesList.filter { it.publisher }
        .map(GameCompanyDTO::toDomainGameCompany),
)

/**
 * Converts this Game Genre DTO (Data Transfer Object) instance to the domain's [GameGenre]
 */
fun Genre.toDomainGameGenre(): GameGenre {
    return try {
        GameGenre.valueOf(name.uppercase())
    } catch (e: IllegalArgumentException) {
        GameGenre.OTHERS
    }
}

/**
 * Converts this Game Platform DTO (Data Transfer Object) instance to the domain's [Platform]
 */
fun PlatformDTO.toDomainGamePlatform(): Platform {
    val platformName = name.lowercase()
    return when {
        platformName.contains("playstation") -> Platform.PLAYSTATION
        platformName.contains("xbox") -> Platform.XBOX
        platformName.contains("microsoft windows") -> Platform.WINDOWS
        platformName.contains("wii") -> Platform.WII
        platformName == "nintendo switch" -> Platform.NINTENDO_SWITCH
        platformName == "android" -> Platform.ANDROID
        platformName == "mac" || platformName == "ios" -> Platform.APPLE
        platformName == "linux" -> Platform.LINUX
        else -> Platform.OTHERS.apply { this.platformName = this@toDomainGamePlatform.name }
    }
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
        else -> GameMode.OTHERS
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
        else -> PlayerPerspective.OTHERS
    }
}

/**
 * Converts this Game Company DTO (Data Transfer Object) instance to the domain's [GameCompany]
 */
fun GameCompanyDTO.toDomainGameCompany(): GameCompany {
    return GameCompany(
        id,
        company.name,
        company.description,
        country = Locale(Locale.getDefault().language, company.country.toString()).displayCountry,
        logoUrl = if (company.hasLogo()) imageBuilder(company.logo.imageId, ImageSize.LOGO_MEDIUM)
        else ""
    )
}


// ------------- Domain Models to Data Transfer Object Converters --------------- //


//fun GameGenre.toDTOName(): String {
//    return when(this){
//        GameGenre.RPG -> "Role-playing (RPG)"
//        else -> toString()
//    }
//}