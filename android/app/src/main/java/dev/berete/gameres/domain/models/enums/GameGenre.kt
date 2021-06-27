package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

enum class GameGenre {
    ADVENTURE,
    ACTION,
    ARCADE,
    RACING,
    RPG,
    SHOOTER,
    SIMULATOR,
    STRATEGY,
    ;

    override fun toString() = name.toLowercaseExceptFirstChar()
}