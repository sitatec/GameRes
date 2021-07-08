package dev.berete.gameres.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import dev.berete.gameres.domain.repositories.GameListRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    ViewModel() {

    private val _gameList: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val gameList: LiveData<List<Game>>
        get() = _gameList

    private val _mostPopularGames: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val mostPopularGames: LiveData<List<Game>>
        get() = _mostPopularGames

    private var currentPage = 0

    private var _loadNextPage = suspend {
        val loadedGames = gameListRepository.getPopularGames(
            startTimeStamp = getYearTimestamp(2017),
            endTimestamp = getYearTimestamp(),
            page = ++currentPage,
        ).sortedByDescending { it.rating }

        _gameList.value = _gameList.value!!.plus(loadedGames)
    }

    var isNextPageLoading by mutableStateOf(false)
        private set

    /**
     * Game genre names to show in the tabs
     */
    val gameGenreNames =
        listOf(*GameGenre.genreNames.toTypedArray(), "Battle Royale", "Multiplayer").sorted()

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGames(
                startTimeStamp = getYearTimestamp(2017),
                endTimestamp = getYearTimestamp(), // NOW
            ))
        }
    }

    private fun setPopularAndHighlyRatedGames(gameList: List<Game>) {
        _mostPopularGames.value = gameList.subList(0, 10)
        _gameList.value =
            gameList.subList(10, gameList.size).sortedByDescending { it.rating }
    }

    fun onGameTypeSelected(genreName: String) {
        when (genreName) {
            "Multiplayer" -> filterByGameMode(GameMode.MULTIPLAYER)
            "Battle Royale" -> filterByGameMode(GameMode.BATTLE_ROYALE)
            else -> filterByGameGenre(GameGenre.valueOf(genreName))
        }
    }

    private fun filterByGameMode(gameMode: GameMode) {
        // TODO filter the current game list first and check if it content enough game and return the
        //  the filtered list before making api call.
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGamesByMode(
                startTimeStamp = getYearTimestamp(2017),
                endTimestamp = getYearTimestamp(),
                gameMode = gameMode,
            ))
        }
        _loadNextPage = {
            val loadedGames = gameListRepository.getPopularGamesByMode(
                startTimeStamp = getYearTimestamp(2017),
                endTimestamp = getYearTimestamp(),
                page = ++currentPage,
                gameMode = gameMode,
            ).sortedByDescending { it.rating }

            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    private fun filterByGameGenre(gameGenre: GameGenre) {
        // TODO filter the current game list first and check if it content enough game and return the
        //  the filtered list before making api call.
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGamesByGenre(
                startTimeStamp = getYearTimestamp(2019),
                endTimestamp = getYearTimestamp(),
                genre = gameGenre,
            ))
        }
        _loadNextPage = {
            val loadedGames = gameListRepository.getPopularGamesByGenre(
                startTimeStamp = getYearTimestamp(2019),
                endTimestamp = getYearTimestamp(),
                page = ++currentPage,
                genre = gameGenre,
            ).sortedByDescending { it.rating }

            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            isNextPageLoading = true
            _loadNextPage()
            isNextPageLoading = false
        }
    }

    /**
     * Returns the timestamp of the given [year] or the current year if no parameter is given,
     * the month will be _January_ and the day will be the first of month (01).
     *
     * I.e the timestamp of 01-01-[year]
     */
    private fun getYearTimestamp(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Long {
        return Calendar.getInstance().apply { set(year, 0, 1) }.timeInMillis
    }
}