package dev.berete.gameres.domain.models

import dev.berete.gameres.domain.models.enums.*
import java.util.Date

/**
 * The class representing a video game.
 */
data class Game(
    val id: Long,
    val name: String,
    val genres: List<GameGenre>,
    val platform: List<Platform>,
    val firstReleaseDate: Date,
    val releaseDates: List<Date>,
    val summary: String,
    val storyline: String,
    val coverUrl: String,
    val artWorkUrls: List<String>,
    val screenshotUrls: List<String>,
    val videoUrls: List<String>,
    val similarGameIds: List<Long>,
    val rating: Double,
    val ratingCount: Int,
    val ageRatings: List<AgeRating>,
    val gameModes: List<GameMode>,
    val playerPerspectives: List<PlayerPerspective>,
    val developers: List<GameCompany>,
    val publishers: List<GameCompany>,
) {}