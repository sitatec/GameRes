package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * A player perspective, e.g: Third person
 */
enum class PlayerPerspective {
    FIRST_PERSON,
    THIRD_PERSON,
    BIRD_VIEW,
    SIDE_VIEW,
    VIRTUAL_REALITY,
    AUDITORY,
    TEXT,
    ;

    /**
     * Returns a capitalized form of the current entry
     */
    override fun toString() = name.replace('_', ' ').toLowercaseExceptFirstChar()

}