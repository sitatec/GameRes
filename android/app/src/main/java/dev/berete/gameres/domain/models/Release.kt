package dev.berete.gameres.domain.models

import java.text.SimpleDateFormat
import java.util.*

class Release(
    val date: Date,
    val platform: Platform,
    val region: String,
    val gameId: Long,
    val gameCoverUrl: String,
    val artWorkUrl: String,
    val gameName: String,
) {
    val formattedDate: String = SimpleDateFormat.getDateInstance().format(date)
}