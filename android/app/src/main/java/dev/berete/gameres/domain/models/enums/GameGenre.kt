package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * A game genre e.g ADVENTURE, STRATEGY
 */
enum class GameGenre {
    ADVENTURE,
    ACTION,
    ARCADE,
    RACING,
    RPG,
    SHOOTER,
    SIMULATOR,
    STRATEGY,
    OTHERS
    ;

    /**
     * Returns a capitalized form of the current entry
     */
    override fun toString() = name.toLowercaseExceptFirstChar()
}