package dev.berete.gameres.ui.utils

import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.PlatformType
import dev.berete.gameres.domain.models.enums.PlatformType.*
import java.text.SimpleDateFormat
import java.util.*

val PlatformType.logo: Int
    get() = when (this) {
        PLAYSTATION -> R.drawable.ic_playstation_logo
        XBOX -> R.drawable.ic_xbox_logo
        WINDOWS -> R.drawable.ic_windows_logo
        NINTENDO_SWITCH -> R.drawable.ic_nintendo_switch_logo
        ANDROID -> R.drawable.ic_android_logo
        APPLE -> R.drawable.ic_apple_logo
        LINUX -> R.drawable.ic_linux_logo
        WII -> R.drawable.ic_wii_logo
        OTHER -> R.drawable.ic_other
    }

val Game.bannerUrl: String
    get() = when {
        artWorkUrls.isNotEmpty() -> artWorkUrls.first()
        screenshotUrls.isNotEmpty() -> screenshotUrls.first()
        else -> "" // TODO create a placeholder
    }

val Game.allImageUrls: List<String>
    get() = artWorkUrls + screenshotUrls

val Game.formattedInitialReleaseDate: String
    get() {
        return SimpleDateFormat.getDateInstance().format(firstReleaseDate!!)
    }

/**
 * Game genre names to show in the tabs
 */
val gameTypeNames: List<String>
    get() = listOf("ALL") + listOf(
        *GameGenre.genreNames.toTypedArray(),
        "BATTLE ROYALE",
        "MULTIPLAYER"
    ).sorted()

/**
 * Returns the timestamp of the given [year] or the current year if no parameter is given,
 * the month will be _January_ and the day will be the first of month (01).
 *
 * I.e the timestamp of 01-01-[year]
 */
fun getYearTimestamp(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Long {
    return Calendar.getInstance().apply { set(year, 0, 1) }.timeInMillis
}