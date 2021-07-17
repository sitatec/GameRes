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
    OTHER
    ;

    /**
     * Returns a capitalized form of the current entry
     */
    override fun toString() = name.toLowercaseExceptFirstChar()

    companion object{

        /**
         * All values except [OTHER]
         */
        val genreValues = values().filter { it != OTHER }

        /**
         * All value names except for [OTHER]
         */
        val genreNames = genreValues.map { it.name }
    }
}