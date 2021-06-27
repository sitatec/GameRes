package dev.berete.gameres.domain.models

import dev.berete.gameres.domain.models.enums.*
import java.util.Date

data class Game(
    val name: String,
    val genres: List<GameGenre>,
    val platform: Platform,
    val firstReleaseDate: Date,
    val releaseDates: List<Date>,
    val summary: String,
    val storyline: String,
    val coverUrl: String,
    val artWorkUrls: List<String>,
    val screenshotUrls: List<String>,
    val videoUrls: List<String>,
    val similarGameIds: List<String>,
    val rating: Int,
    val ratingCount: Int,
    val ageRatings: List<AgeRating>,
    val gameModes: List<GameMode>,
    val playerPerspectives: List<PlayerPerspective>,
    val developers: List<GameCompany>,
    val publishers: List<GameCompany>,
) {}