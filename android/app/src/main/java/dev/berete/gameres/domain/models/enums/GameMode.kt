package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * A game mode, e.g: Multiplayer, Battle Royale
 */
enum class GameMode {
    BATTLE_ROYALE,
    MULTIPLAYER,
    SINGLE_PLAYER,
    MMO, //Massively Multiplayer Online
    SPLIT_SCREEN,
    COOPERATIVE,
    OTHER
    ;

    /**
     * Returns a capitalized form of the current entry
     */
    override fun toString(): String {
        if (this == MMO) {
            return "Massively Multiplayer Online (MMO)"
        }
        return name.replace('_', ' ').toLowercaseExceptFirstChar()
    }
}