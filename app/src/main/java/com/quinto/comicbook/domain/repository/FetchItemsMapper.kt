package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.data.remote.OrderBy

fun getFetchItems(itemType: String, repository: ComicBookRepository) = when (itemType) {
    "characters" -> repository::getCharacters
    "comics" -> repository::getComics
    "creators" -> repository::getCreators
    "events" -> repository::getEvents
    "series" -> repository::getSeries
    "stories" -> repository::getStories
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}

fun getDefaultOrderBy(itemType: String) = when (itemType) {
    "characters" -> OrderBy.NAME
    "comics" -> OrderBy.TITLE
    "creators" -> OrderBy.LAST_NAME
    "events" -> OrderBy.NAME
    "series" -> OrderBy.TITLE
    "stories" -> OrderBy.MODIFIED_DESC
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}