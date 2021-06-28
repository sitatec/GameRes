package dev.berete.gameres.domain.models

/**
 * A game company, e.g: Ubisoft
 */
data class GameCompany(
    val id: Long,
    val name: String,
    val description: String,
    val country: String,
    val logoUrl: String
)