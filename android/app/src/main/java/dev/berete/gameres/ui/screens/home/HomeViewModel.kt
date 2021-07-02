package dev.berete.gameres.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.repositories.GameListRepository

class HomeViewModel(private val gameListRepository: GameListRepository): ViewModel() {

    private val _gameList: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val gameList: LiveData<List<Game>>
        get() = _gameList
}