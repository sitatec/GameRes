package dev.berete.gameres.ui.screens.new_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import dev.berete.gameres.domain.repositories.GameListRepository
import dev.berete.gameres.ui.screens.shared.view_models.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewGamesViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    BaseViewModel() {

    private var minReleaseTimestamp = 0L

    private var _newGames = MutableLiveData<List<Game>>()
    val newGame: LiveData<List<Game>> = _newGames

    init {
        fetchNextPage = {
            _newGames.value =
                _newGames.value!!.plus(
                    gameListRepository.getGamesReleasedAfter(minReleaseTimestamp, ++currentPage)
                )
        }
    }

    fun initialize(minReleaseTimestamp: Long) {
        this.minReleaseTimestamp = minReleaseTimestamp
        fetchNewGames()
    }

    private fun fetchNewGames() {
        viewModelScope.launch {
            _newGames.value =
                gameListRepository.getGamesReleasedAfter(minReleaseTimestamp, currentPage)
        }
    }


    override fun resetFilter() {
        currentPage = 0
        fetchNewGames()
    }

    override fun filterByGameMode(gameMode: GameMode) {
        currentPage = 0
        fetchNextPage = {
            _newGames.value =
                _newGames.value!!.plus(
                    gameListRepository.getGamesReleasedAfter(
                        minReleaseTimestamp,
                        currentPage++,
                        gameMode = gameMode
                    )
                )
        }
        _newGames.value = emptyList()
        viewModelScope.launch {
            fetchNextPage()
        }
    }

    override fun filterByGameGenre(gameGenre: GameGenre) {
        currentPage = 0
        fetchNextPage = {
            _newGames.value =
                _newGames.value!!.plus(
                    gameListRepository.getGamesReleasedAfter(
                        minReleaseTimestamp,
                        currentPage++,
                        gameGenre = gameGenre
                    )
                )
        }
        _newGames.value = emptyList()
        viewModelScope.launch {
            fetchNextPage()
        }
    }

}