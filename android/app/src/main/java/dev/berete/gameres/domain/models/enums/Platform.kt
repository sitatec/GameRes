package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * A Game platform e.g XBOX
 */
enum class Platform {
    PLAYSTATION,
    XBOX,
    WINDOWS,
    NINTENDO,
    ANDROID,
    APPLE, // MACOS && IOS
    LINUX,
    WII,
    OTHERS,
    ;

    // Use full for platforms that are not listed above (OTHERS).
    var platformName: String = name

    override fun toString(): String {
        return if (this == OTHERS) platformName
        else name.toLowercaseExceptFirstChar()
    }
}