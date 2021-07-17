package dev.berete.gameres.ui.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.repositories.GameListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    ViewModel() {

    private var _searchResult = MutableLiveData<List<Game>>(emptyList())
    val searchResult = _searchResult

    fun search(query: String){
        viewModelScope.launch {
            _searchResult.value = gameListRepository.searchGames(query)
        }
    }
}