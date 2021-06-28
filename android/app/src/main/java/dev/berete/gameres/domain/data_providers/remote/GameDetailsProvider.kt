package dev.berete.gameres.domain.data_providers.remote

import dev.berete.gameres.domain.models.Game

interface GameDetailsProvider {

    /**
     * Returns the details of the game that has the given [gameId].
     */
    suspend fun getGameDetails(gameId: String): Game
}