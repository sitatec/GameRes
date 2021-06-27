package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

enum class PlayerPerspective {
    FIRST_PERSON,
    THIRD_PERSON,
    BIRD_VIEW,
    SIDE_VIEW,
    VIRTUAL_REALITY,
    AUDITORY,
    TEXT,
    ;

    override fun toString() = name.replace('_', ' ').toLowercaseExceptFirstChar()

}