package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * Game age rating
 */
class AgeRating(val summary: String, val imageUrl: String, category: Category) {

    /**
     * Game age rating category
     */
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
        ;

        override fun toString(): String {
            return name.toLowercaseExceptFirstChar()
        }
    }
}