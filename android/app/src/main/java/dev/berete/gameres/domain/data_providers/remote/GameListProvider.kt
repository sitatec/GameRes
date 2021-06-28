package dev.berete.gameres.domain.data_providers.remote

import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre

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
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game>

    /**
     * Returns the games that have been released after the givqen [timestamp].
     * Thi function can be useful for fetching new games.
     */
    suspend fun getGamesReleasedAfter(
        timestamp: Long,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game>

    /**
     * Returns the games that will be released before the given [timestamp]
     */
    suspend fun getUpcomingGames(
        timestamp: Long,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game>

    /**
     * Returns the popular games (from [startTimeStamp] to [endTimestamp]) of the given [genre].
     *
     * e.g: Action games that were popular in the interval of 23/04/2017 to 18/11/2019
     */
    suspend fun getPopularGamesByGenre(
        startTimeStamp: Long,
        endTimestamp: Long,
        genre: GameGenre,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game>

    companion object {
        const val MAX_GAME_COUNT_BY_REQUEST = 100
        const val DEFAULT_GAME_COUNT_BY_REQUEST = 30
    }

}