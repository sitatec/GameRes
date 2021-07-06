package dev.berete.gameres.domain.models

import dev.berete.gameres.domain.models.enums.*
import java.util.Date

/**
 * The class representing a video game.
 */
data class Game(
    val id: Long,
    val name: String,
    val coverUrl: String,
    val rating: Double,
    val ratingCount: Int,
    val platformList: List<Platform>,
    val artWorkUrls: List<String> = emptyList(),
    val genres: List<GameGenre> = emptyList(),
    val firstReleaseDate: Date? = null,
    val releaseDates: List<Date> = emptyList(),
    val summary: String = "",
    val storyline: String = "",
    val screenshotUrls: List<String> = emptyList(),
    val videoUrls: List<String> = emptyList(),
    val similarGameIds: List<Long> = emptyList(),
    val ageRatings: List<AgeRating> = emptyList(),
    val gameModes: List<GameMode> = emptyList(),
    val playerPerspectives: List<PlayerPerspective> = emptyList(),
    val developers: List<GameCompany> = emptyList(),
    val publishers: List<GameCompany> = emptyList(),
) {}