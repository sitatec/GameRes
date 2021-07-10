package dev.berete.gameres.ui.screens.game_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.repositories.GameDetailsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(private val gameDetailsRepository: GameDetailsRepository) :
    ViewModel() {

    private val _game = MutableLiveData<Game>()

    fun getGame(gameId: Long): LiveData<Game>{
        viewModelScope.launch {
            _game.value = gameDetailsRepository.getGameDetails(gameId)
        }
        return _game
    }
}