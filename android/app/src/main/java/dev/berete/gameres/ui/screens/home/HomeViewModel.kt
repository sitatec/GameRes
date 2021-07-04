package dev.berete.gameres.ui.screens.home

import android.util.Log
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
import proto.GameCategoryEnum
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    ViewModel() {

    private val _trendingGameList: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val trendingGameList: LiveData<List<Game>>
        get() = _trendingGameList

    private val _mostPopularGames: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val mostPopularGames: LiveData<List<Game>>
        get() = _mostPopularGames

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
            setGames(gameListRepository.getPopularGames(
                startTimeStamp = getYearTimestamp(2019),
                endTimestamp = getYearTimestamp(), // NOW
            ))
        }
    }

    private fun setGames(gameList: List<Game>) {
        Log.d("HOME_VIEW_MODEL", "setGames")
        _mostPopularGames.value = gameList.subList(0, 10)
        _trendingGameList.value =
            gameList.subList(10, gameList.size).sortedByDescending { it.rating }
    }

    fun onGameGenreSelected(genreName: String) {
        when (genreName) {
            "Multiplayer" -> filterByGameMode(GameMode.MULTIPLAYER)
            "Battle Royale" -> filterByGameMode(GameMode.BATTLE_ROYALE)
            else -> filterByGameGenre(GameGenre.valueOf(genreName))
        }
    }


    private fun filterByGameMode(gameMode: GameMode) {

    }

    private fun filterByGameGenre(gameGenre: GameGenre) {
        viewModelScope.launch {
            setGames(gameListRepository.getPopularGamesByGenre(
                startTimeStamp = getYearTimestamp(2019),
                endTimestamp = getYearTimestamp(),
                genre = gameGenre,
            ))
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