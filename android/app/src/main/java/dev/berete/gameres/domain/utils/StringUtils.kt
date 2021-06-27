package dev.berete.gameres.domain.utils

import java.util.Locale

fun String.toLowercaseExceptFirstChar() : String{
    return lowercase(Locale.getDefault()).replaceFirstChar( Char::uppercaseChar )
}