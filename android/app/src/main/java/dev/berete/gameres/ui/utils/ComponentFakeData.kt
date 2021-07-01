package dev.berete.gameres.ui.utils

import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.Platform

val FakeGame = Game(
    id= 0,
    name = "Apex Legend",
    coverUrl = "https://en.wikipedia.org/wiki/File:Apex_legends_cover.jpg",
    rating = 4.8,
    ratingCount = 289,
    platformList = listOf(Platform.WINDOWS, Platform.XBOX, Platform.PLAYSTATION, Platform.ANDROID),
    artWorkUrls = listOf("https://media.contentapi.ea.com/content/dam/news/www-ea/images/2019/04/apex-featured-image-generic-lineup.jpg.adapt.crop191x100.628p.jpg"),
)