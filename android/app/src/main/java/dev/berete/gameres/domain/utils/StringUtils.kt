package dev.berete.gameres.domain.utils

import java.util.Locale

/**
 * Returns the uppercase form of the string on which this function is called
 * This function is useful for strings that contain uppercase letters.
 *
 * e.g: SAMPLE => Sample, samPLE => Sample
 */
fun String.toLowercaseExceptFirstChar() : String{
    if(length == 1) return this
    return lowercase(Locale.getDefault()).replaceFirstChar( Char::uppercaseChar )
}