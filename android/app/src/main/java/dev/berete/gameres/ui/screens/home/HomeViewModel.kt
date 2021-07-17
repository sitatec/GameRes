package dev.berete.gameres.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.Release
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import dev.berete.gameres.domain.repositories.GameListRepository
import dev.berete.gameres.ui.screens.shared.view_models.BaseViewModel
import dev.berete.gameres.ui.utils.getYearTimestamp
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    BaseViewModel() {

    /**
     * A random timestamp from 2000 to the last year, so each time the user launches the app,
     * he/she will see different games on the home page.
     *
     * TODO implement a smartest algo.
     */
    private val minReleaseDate: Long by lazy {
            val numberOfYearsSince2000 = Calendar.getInstance().get(Calendar.YEAR) - 2000
            val randomYearFrom2000toLastYear = Random().nextInt(numberOfYearsSince2000 - 1) + 2000
            getYearTimestamp(randomYearFrom2000toLastYear)
        }

    private val _gameList: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val gameList: LiveData<List<Game>> = _gameList

    private val _mostPopularGames: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val mostPopularGames: LiveData<List<Game>> = _mostPopularGames

    private val _newGames: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val newGames: LiveData<List<Game>> = _newGames

    private val _upComingReleases: MutableLiveData<List<Release>> = MutableLiveData(emptyList())
    val upComingReleases: LiveData<List<Release>> = _upComingReleases

    init {
        fetchNewGames()
        fetchGameList()
        fetchUpComingGames()

        fetchNextPage =  {
            val loadedGames = gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(),
                page = ++currentPage,
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST

            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    private fun fetchNewGames() {
        viewModelScope.launch {
            val lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1
            _newGames.value = gameListRepository.getGamesReleasedAfter(
                timestamp = getYearTimestamp(lastYear),
                count = 10,
            )
        }
    }

    private fun fetchGameList() {
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(), // NOW
            ))
        }
    }

    private fun fetchUpComingGames(){
        viewModelScope.launch {
            _upComingReleases.value = gameListRepository.getUpcomingReleases(count = 10)
        }
    }

    private fun setPopularAndHighlyRatedGames(gameList: List<Game>) {
        _mostPopularGames.value = gameList.subList(0, 10)
        _gameList.value = gameList.subList(10, gameList.size).sortedByDescending { it.rating }
    }

    override fun resetFilter() {
        fetchGameList()
    }

    override fun filterByGameMode(gameMode: GameMode) {
        // TODO filter the current game list first and check if it content enough game and return the
        //  the filtered list before making api call.
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(),
                gameMode = gameMode,
            ))
        }
        fetchNextPage = {
            val loadedGames = gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(),
                page = ++currentPage,
                gameMode = gameMode,
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    override fun filterByGameGenre(gameGenre: GameGenre) {
        // TODO filter the current game list first and check if it content enough game and return the
        //  the filtered list before making api call.
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(),
                gameGenre = gameGenre,
            ))
        }
        fetchNextPage = {
            val loadedGames = gameListRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimestamp = getYearTimestamp(),
                page = ++currentPage,
                gameGenre = gameGenre,
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

}