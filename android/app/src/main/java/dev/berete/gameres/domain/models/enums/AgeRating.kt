package dev.berete.gameres.domain.models.enums

import dev.berete.gameres.domain.utils.toLowercaseExceptFirstChar

/**
 * Game age rating
 */
enum class AgeRating(val labelUrl: String = "") {
    THREE("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/PEGI_3.svg/197px-PEGI_3.svg.png"),
    SEVEN("https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/PEGI_7.svg/197px-PEGI_7.svg.png"),
    TWELVE("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/PEGI_12.svg/197px-PEGI_12.svg.png"),
    SIXTEEN("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/PEGI_16.svg/197px-PEGI_16.svg.png"),
    EIGHTEEN("https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/PEGI_18.svg/196px-PEGI_18.svg.png"),
    RP("https://www.esrb.org/wp-content/uploads/2019/05/RP.svg"),
    EC,
    E("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/ESRB_2013_Everyone.svg/160px-ESRB_2013_Everyone.svg.png"),
    E10("https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/ESRB_2013_Everyone_10%2B.svg/160px-ESRB_2013_Everyone_10%2B.svg.png"),
    T("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/ESRB_2013_Teen.svg/160px-ESRB_2013_Teen.svg.png"),
    M("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/ESRB_2013_Mature.svg/160px-ESRB_2013_Mature.svg.png"),
    AO("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/ESRB_2013_Adults_Only.svg/160px-ESRB_2013_Adults_Only.svg.png"),
    UNRECOGNIZED,
    ;

    override fun toString(): String {
        return name.toLowercaseExceptFirstChar()
    }

}