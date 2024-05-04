package com.quinto.comicbook.domain.repository

fun getFetchItems(itemType: String, repository: ComicBookRepository) = when (itemType) {
    "characters" -> repository::getCharacters
    "comics" -> repository::getComics
    "creators" -> repository::getCreators
    "events" -> repository::getEvents
    "series" -> repository::getSeries
    "stories" -> repository::getStories
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}