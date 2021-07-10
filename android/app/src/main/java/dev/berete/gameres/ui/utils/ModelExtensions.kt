package dev.berete.gameres.ui.utils

import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.Platform
import dev.berete.gameres.domain.models.enums.Platform.*

val Platform.logo: Int
    get() = when (this) {
        PLAYSTATION -> R.drawable.ic_playstation_logo
        XBOX -> R.drawable.ic_xbox_logo
        WINDOWS -> R.drawable.ic_windows_logo
        NINTENDO_SWITCH -> R.drawable.ic_nintendo_switch_logo
        ANDROID -> R.drawable.ic_android_logo
        APPLE -> R.drawable.ic_apple_logo
        LINUX -> R.drawable.ic_linux_logo
        WII -> R.drawable.ic_wii_logo
        OTHERS -> R.drawable.ic_other
    }

val Game.bannerUrl: String
    get() = when {
        artWorkUrls.isNotEmpty() -> artWorkUrls.first()
        screenshotUrls.isNotEmpty() -> screenshotUrls.first()
        else -> "" // TODO create a placeholder
    }

val Game.allImageUrls: List<String>
    get() = artWorkUrls + screenshotUrls