package dev.berete.gameres.ui.screens.popular_games

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import dev.berete.gameres.domain.repositories.GameListRepository
import dev.berete.gameres.ui.screens.shared.view_models.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PopularGamesViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    BaseViewModel() {

    private var _popularGames = MutableLiveData<List<Game>>(emptyList())
    val popularGames = _popularGames

    private var gameGareFilter: GameGenre? = null
    private var gameModeFilter: GameMode? = null
    private var isInitialized = false

    fun initializes(startTimestamp: Long) {
        // The `NavHost` call the `composable` function twice the first time, so we prevent fetching
        // the data twice.
        if (isInitialized) return
        isInitialized = true

        fetchNextPage = {
            _popularGames.value = _popularGames.value!! + gameListRepository.getPopularGames(
                startTimeStamp = startTimestamp,
                endTimestamp = Calendar.getInstance().timeInMillis, // NOW
                page = currentPage++,
                gameMode = gameModeFilter,
                gameGenre = gameGareFilter,
            ).apply {
                isLastPageReached = size < GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            }
        }

        getNextPage()
    }

    private fun getNextPage() {
        viewModelScope.launch {
            fetchNextPage()
        }
    }

    override fun resetFilter() {
        resetPages()
        getNextPage()
    }

    override fun filterByGameMode(gameMode: GameMode) {
        resetPages()
        gameModeFilter = gameMode
        getNextPage()
    }

    override fun filterByGameGenre(gameGenre: GameGenre) {
        resetPages()
        gameGareFilter = gameGenre
        getNextPage()
    }

    private fun resetPages() {
        gameGareFilter = null
        gameModeFilter = null
        _popularGames.value = emptyList()
        currentPage = 0
    }
}