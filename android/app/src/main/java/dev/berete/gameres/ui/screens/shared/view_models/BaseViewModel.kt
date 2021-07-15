package dev.berete.gameres.ui.screens.shared.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.domain.models.enums.GameMode
import kotlinx.coroutines.launch
import java.util.*

abstract class BaseViewModel : ViewModel() {

    protected var currentPage = 0

    protected lateinit var fetchNextPage: suspend () -> Unit

    private var isNextPageLoading by mutableStateOf(false)

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
        viewModelScope.launch {
            isNextPageLoading = true
            fetchNextPage()
            isNextPageLoading = false
        }
    }

    /**
     * Returns the timestamp of the given [year] or the current year if no parameter is given,
     * the month will be _January_ and the day will be the first of month (01).
     *
     * I.e the timestamp of 01-01-[year]
     */
    protected fun getYearTimestamp(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Long {
        return Calendar.getInstance().apply { set(year, 0, 1) }.timeInMillis
    }
}