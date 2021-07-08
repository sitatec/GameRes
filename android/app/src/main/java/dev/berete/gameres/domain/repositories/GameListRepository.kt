package dev.berete.gameres.domain.repositories

import dev.berete.gameres.domain.data_providers.local.CachedGamesProvider
import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameListRepository @Inject constructor(
    private val gameListProvider: GameListProvider,
//    private val cachedGamesProvider: CachedGamesProvider,
) {
    // TODO handle cache

    /**
     * Returns the popular games in the given interval (from [startTimeStamp] to [endTimestamp]).
     * The result may be retrieved from the local database if an internet connection is not available
     */
    suspend fun getPopularGames(
        startTimeStamp: Long,
        endTimestamp: Long,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game> {
        return gameListProvider.getPopularGames(startTimeStamp, endTimestamp, page,count)
    }

    /**
     * Returns the games that have been released after the given [timestamp].
     * Thi function can be useful for fetching new games.
     * The result may be retrieved from the local database if an internet connection is not available
     */
    suspend fun getGamesReleasedAfter(
        timestamp: Long,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game> {
        return gameListProvider.getGamesReleasedAfter(timestamp, page, count)
    }

    /**
     * Returns the games that will be released before the given [limitTimestamp] and after the current
     * date plus one day.
     * The result may be retrieved from the local database if an internet connection is not available
     */
    suspend fun getUpcomingGames(
        limitTimestamp: Long,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game> {
        return gameListProvider.getUpcomingGames(limitTimestamp, page, count)
    }

    /**
     * Returns the popular games (from [startTimeStamp] to [endTimestamp]) of the given [genre].
     * The result may be retrieved from the local database if an internet connection is not available
     *
     * e.g: Action games that were popular in the interval of 23/04/2017 to 18/11/2019
     */
    suspend fun getPopularGamesByGenre(
        startTimeStamp: Long,
        endTimestamp: Long,
        genre: GameGenre,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game> {
        return gameListProvider.getPopularGamesByGenre(
            startTimeStamp,
            endTimestamp,
            genre,
            page,
            count,
        )
    }

    /**
     * Returns the popular games (from [startTimeStamp] to [endTimestamp]) of the given [gameMode].
     * The result may be retrieved from the local database if an internet connection is not available
     *
     * e.g: Battle Royale games that were popular in the interval of 23/04/2017 to 18/11/2019
     */
    suspend fun getPopularGamesByMode(
        startTimeStamp: Long,
        endTimestamp: Long,
        gameMode: GameMode,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
    ): List<Game> {
        return gameListProvider.getPopularGamesByMode(
            startTimeStamp,
            endTimestamp,
            gameMode,
            page,
            count,
        )
    }

}