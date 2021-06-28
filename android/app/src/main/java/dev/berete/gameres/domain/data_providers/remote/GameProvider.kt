package dev.berete.gameres.domain.data_providers.remote

import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre

/**
 * Game date provider
 */
interface GameProvider {

    /**
     * Returns the popular games int given interval (from [startTimeStamp] to [endTimestamp]).
     */
    suspend fun getPopularGames(startTimeStamp: Long, endTimestamp: Long): List<Game>

    /**
     * Returns the games that have been released after the given [timestamp].
     * Thi function can be useful for fetching new games.
     */
    suspend fun getGamesReleasedAfter(timestamp: Long): List<Game>

    /**
     * Returns the games that will be released before the given [timestamp]
     */
    suspend fun getUpcomingGames(timestamp: Long): List<Game>

    /**
     * Returns the popular games (from [startTimeStamp] to [endTimestamp]) of the given [genre].
     *
     * e.g: Action games that were popular in the interval of 23/04/2017 to 18/11/2019
     */
    suspend fun getPopularGamesByGenre(
        startTimeStamp: Long,
        endTimestamp: Long,
        genre: GameGenre,
    ): List<Game>

}