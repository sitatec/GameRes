package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * A Game PlatformType e.g XBOX or PLAYSTATION
 */
enum class PlatformType {
    PLAYSTATION,
    XBOX,
    WINDOWS,
    NINTENDO_SWITCH,
    ANDROID,
    APPLE, // MACOS && IOS
    LINUX,
    WII,
    OTHER,
    ;

    override fun toString(): String {
        return name.toLowercaseExceptFirstChar()
    }
}