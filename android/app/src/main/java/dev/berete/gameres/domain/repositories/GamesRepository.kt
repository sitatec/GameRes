package dev.berete.gameres.domain.repositories

import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.request.IGDBWrapper

class GamesRepository(val iGDBWrapper : IGDBWrapper, val apiCalypse: APICalypse) {

    init {

    }

    fun getGameList(){

    }
}