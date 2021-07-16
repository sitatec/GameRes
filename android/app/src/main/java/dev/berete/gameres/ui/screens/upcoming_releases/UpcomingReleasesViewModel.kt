package dev.berete.gameres.ui.screens.upcoming_releases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berete.gameres.domain.data_providers.remote.GameListProvider
import dev.berete.gameres.domain.models.Release
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import dev.berete.gameres.domain.repositories.GameListRepository
import dev.berete.gameres.ui.screens.shared.view_models.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingReleasesViewModel @Inject constructor(private val gameListRepository: GameListRepository) :
    BaseViewModel() {

    private var _upcomingReleases = MutableLiveData<List<Release>>(emptyList())
    val upcomingReleases = _upcomingReleases

    private var gameGareFilter: GameGenre? = null
    private var gameModeFilter: GameMode? = null

    init {
        fetchNextPage = {
            _upcomingReleases.value =
                _upcomingReleases.value!! + gameListRepository.getUpcomingReleases(
                    page = currentPage++,
                    gameGenre = gameGareFilter,
                    gameMode = gameModeFilter,
                )
                    .apply {
                        isLastPageReached = size < GameListProvider.DEFAULT_GAME_COUNT_BY_REQUEST
                    }
        }
        getNextPage()
    }

    private fun getNextPage(){
        viewModelScope.launch {
            fetchNextPage()
        }
    }

    override fun resetFilter() {
        resetPages()
        gameGareFilter = null
        gameModeFilter = null
        getNextPage()
    }

    override fun filterByGameMode(gameMode: GameMode) {
        resetPages()
        gameGareFilter = null
        gameModeFilter = gameMode
        getNextPage()
    }

    override fun filterByGameGenre(gameGenre: GameGenre) {
        resetPages()
        gameGareFilter = gameGenre
        gameModeFilter = null
        getNextPage()
    }

    private fun resetPages(){
        _upcomingReleases.value = emptyList()
        currentPage = 0
    }
}