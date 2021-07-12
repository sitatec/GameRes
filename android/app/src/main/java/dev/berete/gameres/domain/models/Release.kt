package dev.berete.gameres.domain.models

import dev.berete.gameres.domain.models.enums.Platform
import java.text.SimpleDateFormat
import java.util.*

class Release(val date: Date, val platform: Platform, val region: String){
    val formattedDate: String = SimpleDateFormat.getDateInstance().format(date)
}