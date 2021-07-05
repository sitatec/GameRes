package dev.berete.gameres.data_sources.remote

import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.apicalypse.Sort
import com.api.igdb.request.IGDBWrapper
import com.api.igdb.request.games
import proto.Game as GameDTO
import dev.berete.gameres.data_sources.remote.access_token.AccessTokenProvider
import dev.berete.gameres.domain.data_providers.remote.GameDetailsProvider
import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.util.*

class IGDBAPIClient(
    private val iGDBAPIWrapper: IGDBWrapper,
    private val apiCalypse: APICalypse,
) : GameListProvider, GameDetailsProvider {

    //TODO REFACTORING: delegate the common code between the get* methods to another function.

    private val gameSummaryFields =
        "name, cover.image_id, total_rating, platforms.name, status, artworks.image_id, total_rating_count"

    private val completeGameFields =
        "$gameSummaryFields, genres, game_modes, age_ratings, first_release_date, themes," +
                "player_perspectives, release_dates, screenshots, similar_games, storyline, summary, videos," +
                "total_rating_count"

    override suspend fun getPopularGames(
        startTimeStamp: Long,
        endTimestamp: Long,
        count: Int,
    ): List<Game> {
        val numberOfGamesToFetch = regularizeGameCount(count)
        val queryBuilder = apiCalypse.newBuilder()
            .fields(gameSummaryFields)
            .where("first_release_date > ${startTimeStamp.toFixed10Digits()} & first_release_date < ${endTimestamp.toFixed10Digits()} & platforms = (${getIGDBPlatformIDs()}) & total_rating_count != 0")
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGamesToFetch)

        return withContext(IO) {
            iGDBAPIWrapper.games(queryBuilder).map(GameDTO::toDomainGame)
        }
    }

    override suspend fun getGamesReleasedAfter(timestamp: Long, count: Int): List<Game> {
        val numberOfGamesToFetch = regularizeGameCount(count)
        val queryBuilder = apiCalypse.newBuilder()
            .fields(gameSummaryFields)
            .where("first_release_date > ${timestamp.toFixed10Digits()} & platforms = (${getIGDBPlatformIDs()}) & total_rating_count != 0")
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGamesToFetch)

        return withContext(IO) {
            iGDBAPIWrapper.games(queryBuilder).map(GameDTO::toDomainGame)
        }
    }

    override suspend fun getUpcomingGames(limitTimestamp: Long, count: Int): List<Game> {
        val numberOfGamesToFetch = regularizeGameCount(count)
        val tomorrowTimeStamp =
            Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }.timeInMillis
        val queryBuilder = apiCalypse.newBuilder()
            .fields(gameSummaryFields)
            .where("first_release_date > ${tomorrowTimeStamp.toFixed10Digits()} & first_release_date < ${limitTimestamp.toFixed10Digits()} & platforms = (${getIGDBPlatformIDs()}) & total_rating_count != 0")
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGamesToFetch)

        return withContext(IO) {
            iGDBAPIWrapper.games(queryBuilder).map(GameDTO::toDomainGame)
        }
    }

    override suspend fun getPopularGamesByGenre(
        startTimeStamp: Long,
        endTimestamp: Long,
        genre: GameGenre,
        count: Int,
    ): List<Game> {
        val numberOfGamesToFetch = regularizeGameCount(count)
        val queryBuilder = apiCalypse.newBuilder()
            .fields(gameSummaryFields)
            .where("first_release_date > ${startTimeStamp.toFixed10Digits()} & first_release_date < ${endTimestamp.toFixed10Digits()} & ${
                getGameGenreQuery(genre)
            } & platforms = (${getIGDBPlatformIDs()}) & total_rating_count != 0")
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGamesToFetch)

        return withContext(IO) {
            iGDBAPIWrapper.games(queryBuilder).map(GameDTO::toDomainGame)
        }
    }

    override suspend fun getGameDetails(gameId: Long): Game {
        val queryBuilder = apiCalypse.newBuilder().fields(completeGameFields).where("id = $gameId")

        return withContext(IO) {
            iGDBAPIWrapper.games(queryBuilder).first().toDomainGame()
        }
    }

    // --------------- UTILS FUNCTIONS ------------------ //

    private fun regularizeGameCount(count: Int): Int {
        return when {
            count > GameListProvider.MAX_GAME_COUNT_BY_REQUEST -> GameListProvider.MAX_GAME_COUNT_BY_REQUEST
            count < 1 -> throw IllegalArgumentException("The parameter [count] most be a positive number")
            else -> count
        }
    }

    private fun getGameGenreQuery(genre: GameGenre): String {
        if(genre == GameGenre.ACTION) return "themes.name = \"Action\""
        if (genre != GameGenre.OTHERS) return "genres.name = \"${genre.iGDBCompatibleName()}\""

        val allGenreNames = GameGenre.genreValues.map(GameGenre::iGDBCompatibleName)
        return "genres.name != (${allGenreNames.joinToString()}})"
    }

    private fun getIGDBPlatformIDs(): String {
        val playstationIds = "167, 48, 9, 8, 165, 46, 38, 131" // PLAYSTATION 5, PLAYSTATION 4,...
        val xboxIds = "169, 49, 12, 11" // Xbox Series, Xbox One, Xbox 360...
        val windowsId = "6"
        val nintendoSwitchId = "130"
        val android = "34"
        val appleIds = "14, 39" // MacOS, IOS
        val linuxId = "39"
        val wiiIds = "5, 41" // WII, WII U
        return "$playstationIds, $xboxIds, $windowsId, $nintendoSwitchId, $android, $appleIds, $linuxId, $wiiIds"
    }

}

// --------------- EXTENSIONS FUNCTIONS ------------------ //

/**
 * Return a new instance of [APICalypse] to make testing easy. As the [APICalypse] is not reusable
 * because there is no way to reset the parameters of the last query, and we can't instantiate
 * [IGDBAPIClient] each time we need a new [APICalypse] (or to reset the last query parameters),
 * and if we instantiate [APICalypse] inside the [IGDBAPIClient]'s methods we will not be able to
 * test [IGDBAPIClient] efficiently.
 *
 * Now we can pass a single instance of [APICalypse] and each time we need a new instance of it
 * (or to reset the last query parameters), we call this function, so we can mock it and return
 * fake object we've created for the tests.
 */
fun APICalypse.newBuilder() = APICalypse()

/**
 * Return a game genre name that is compatible with (or match) the IGDB Game genre naming
 */
fun GameGenre.iGDBCompatibleName() =
    if (this == GameGenre.RPG) "Role-playing (RPG)" else name.toLowercaseExceptFirstChar()

fun Long.toFixed10Digits() : String {
    val thisNumberAsString = this.toString()
    if(thisNumberAsString.length > 10){
         thisNumberAsString.substring(0..9)
    }
    return thisNumberAsString
}