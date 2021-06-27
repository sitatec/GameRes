package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

enum class GameMode {
    BATTLE_ROYALE,
    MULTIPLAYER,
    SINGLE_PLAYER,
    MMO, //Massively Multiplayer Online
    SPLIT_SCREEN,
    COOPERATIVE,
    ;

    override fun toString(): String {
        if (this == MMO) {
            return "Massively Multiplayer Online (MMO)"
        }
        return name.replace('_', ' ').toLowercaseExceptFirstChar()
    }
}