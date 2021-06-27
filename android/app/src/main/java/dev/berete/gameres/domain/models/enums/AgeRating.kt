package dev.berete.gameres.domain.models.enums

class AgeRating(val summary: String, val imageUrl: String, category: Category) {

    enum class Category {
        THREE,
        SEVEN,
        TWELVE,
        SIXTEEN,
        EIGHTEEN,
        RP,
        EC,
        E,
        E10,
        T,
        M,
        AO,
        UNRECOGNIZED,
    }
}