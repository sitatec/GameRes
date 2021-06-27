package dev.berete.gameres.domain.models

data class GameCompany(
    val name: String,
    val description: String,
    val country: String,
    val parentCompany: GameCompany?,
    val childrenCompanies: List<GameCompany>,
) {
    val hasParentCompany = parentCompany != null
    val hasChildrenCompanies = childrenCompanies.isNotEmpty()
}