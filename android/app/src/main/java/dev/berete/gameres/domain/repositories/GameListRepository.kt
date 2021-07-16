package dev.berete.gameres.domain.repositories

import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.Release
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
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Game> {
        return gameListProvider.getPopularGames(startTimeStamp, endTimestamp, page,count, gameGenre, gameMode)
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
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Game> {
        return gameListProvider.getGamesReleasedAfter(timestamp, page, count, gameGenre, gameMode)
    }

    /**
     * Returns the [Release]s that will happen before the given [limitTimestamp] and after the current
     * date plus one day, if no [limitTimestamp] is given, there will not be a limit date.
     * The result may be retrieved from the local database if an internet connection is not available
     */
    suspend fun getUpcomingReleases(
        limitTimestamp: Long = 0,
        page: Int = 0,
        count: Int = GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GameGenre? = null,
        gameMode: GameMode? = null,
    ): List<Release> {
        return gameListProvider.getUpcomingReleases(limitTimestamp, page, count, gameGenre, gameMode)
    }


    /**
     * Return the games that have their id in the [gameIds].
     */
    suspend fun getGamesByIds(gameIds: List<Long>): List<Game> {
        return gameListProvider.getGamesByIds(gameIds)
    }

}