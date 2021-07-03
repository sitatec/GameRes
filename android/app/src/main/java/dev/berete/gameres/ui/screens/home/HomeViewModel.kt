package dev.berete.gameres.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.repositories.GameListRepository
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            val gameList = gameListRepository.getPopularGames(
                startTimeStamp = Calendar.getInstance().apply { set(2019, 0, 1) }.timeInMillis,
                endTimestamp = Calendar.getInstance().timeInMillis, // NOW
            )
            _mostPopularGames.value = gameList.subList(0, 10)
            _trendingGameList.value = gameList.subList(10, gameList.size)
        }
    }

}