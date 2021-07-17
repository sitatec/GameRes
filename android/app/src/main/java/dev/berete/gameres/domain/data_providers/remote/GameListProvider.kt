package dev.berete.gameres.domain.data_providers.remote

import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.Release
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode

/**
 * Game date provider
 */
interface GameListProvider {

    /**
     * Returns the popular games in the given interval (from [startTimeStamp] to [endTimestamp]).
     */
    suspend fun getPopularGames(
        startTimeStamp: Long,
        endTimestamp: Long,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Game>

    /**
     * Returns the games that have been released after the given [timestamp].
     * Thi function can be useful for fetching new games.
     */
    suspend fun getGamesReleasedAfter(
        timestamp: Long,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Game>

    /**
     * Returns the [Release]s that will happen before the given [limitTimestamp] and after the current
     * date plus one day, if no [limitTimestamp] is given, there will not be a limit date.
     */
    // TODO create a release provider interface and move this method their.
    suspend fun getUpcomingReleases(
        limitTimestamp: Long = 0,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Release>

    /**
     * Return the games that have their id in the [gameIds] list.
     */
    suspend fun getGamesByIds(gameIds: List<Long>): List<Game>

    /**
     * Return a list of games whose names match the given [query].
     */
    suspend fun searchGames(query: String): List<Game>

    companion object {
        const val MAX_GAME_COUNT_BY_REQUEST = 100
        const val DEFAULT_GAME_COUNT_BY_REQUEST = 30
    }

}