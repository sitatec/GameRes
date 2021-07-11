package dev.berete.gameres.ui.screens.game_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.repositories.GameDetailsRepository
import dev.berete.gameres.domain.repositories.GameListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gameDetailsRepository: GameDetailsRepository,
    private val gameListRepository: GameListRepository,
) : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _similarGames = MutableLiveData<List<Game>>()
    val similarGames = _similarGames

    fun initialize(gameId: Long) {
        viewModelScope.launch {
            _game.value = gameDetailsRepository.getGameDetails(gameId)
            _similarGames.value = gameListRepository.getGamesByIds(game.value!!.similarGameIds)
        }
    }
}