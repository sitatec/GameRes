package dev.berete.gameres.domain.data_providers.local

import dev.berete.gameres.domain.models.Game

/**
 * Games provider from cache (local database)
 */
interface CachedGamesProvider {

    /**
     * Returns cached games sorted by popularity (the most popular first) if there are any,
     * otherwise an empty list.
     */
    suspend fun getGamesSortedByPopularity() : List<Game>

    /**
     * Returns the most recently games viewed by the current user.
     */
    suspend fun getRecentlyViewedGames() : List<Game>
}