package dev.berete.gameres.ui.screens.shared.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected var currentPage = 0
    protected lateinit var fetchNextPage: suspend () -> Unit
    var isLastPageReached by mutableStateOf(false)
        protected set
    var isNextPageLoading by mutableStateOf(false)
        private set

    fun onGameTypeSelected(genreName: String) {
        when (genreName) {
            "ALL" -> resetFilter()
            "MULTIPLAYER" -> filterByGameMode(GameMode.MULTIPLAYER)
            "BATTLE ROYALE" -> filterByGameMode(GameMode.BATTLE_ROYALE)
            else -> filterByGameGenre(GameGenre.valueOf(genreName))
        }
    }

    protected abstract fun resetFilter()

    protected abstract fun filterByGameMode(gameMode: GameMode)

    protected abstract fun filterByGameGenre(gameGenre: GameGenre)

    fun loadNextPage() {
        if(isLastPageReached) return

        viewModelScope.launch {
            isNextPageLoading = true
            fetchNextPage()
            isNextPageLoading = false
        }
    }
}