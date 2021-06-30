package dev.berete.gameres.domain.repositories

import dev.berete.gameres.domain.data_providers.local.CachedGamesProvider
import dev.berete.gameres.domain.data_providers.remote.GameDetailsProvider
import dev.berete.gameres.domain.models.Game

class GameDetailsRepository(
    private val gameDetailsProvider: GameDetailsProvider,
    private val cachedGamesProvider: CachedGamesProvider,
) {
    // TODO handle cache

    /**
     * Returns the details of the game that has the given [gameId].
     * The result may be retrieved from the local database if an internet connection is not available
     */
    suspend fun getGameDetails(gameId: Long): Game {
        return gameDetailsProvider.getGameDetails(gameId)
    }
}