package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.domain.model.ItemType

fun getFetchItems(itemType: String, repository: ComicBookRepository) = when (itemType) {
    ItemType.CHARACTER.typeName -> repository::getCharacters
    ItemType.COMIC.typeName -> repository::getComics
    ItemType.CREATOR.typeName -> repository::getCreators
    ItemType.EVENT.typeName -> repository::getEvents
    ItemType.SERIES.typeName -> repository::getSeries
    ItemType.STORY.typeName -> repository::getStories
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}

fun getItemTypesForDetail(itemType: String) = when (itemType) {
    ItemType.CHARACTER.typeName -> listOf(ItemType.COMIC, ItemType.SERIES, ItemType.EVENT, ItemType.STORY)
    ItemType.COMIC.typeName -> listOf(ItemType.CHARACTER, ItemType.CREATOR, ItemType.EVENT, ItemType.STORY)
    ItemType.CREATOR.typeName -> listOf(ItemType.COMIC, ItemType.SERIES, ItemType.EVENT, ItemType.STORY)
    ItemType.EVENT.typeName -> listOf(ItemType.COMIC, ItemType.CHARACTER, ItemType.CREATOR, ItemType.SERIES, ItemType.STORY)
    ItemType.SERIES.typeName -> listOf(ItemType.COMIC, ItemType.CHARACTER, ItemType.CREATOR, ItemType.EVENT, ItemType.STORY)
    ItemType.STORY.typeName -> listOf(ItemType.COMIC, ItemType.CHARACTER, ItemType.CREATOR, ItemType.EVENT, ItemType.SERIES, )
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}

fun getItemTypesForHome() = listOf(
    ItemType.COMIC,
    ItemType.CHARACTER,
    ItemType.SERIES,
    ItemType.CREATOR,
    ItemType.EVENT,
    ItemType.STORY
)

fun getDefaultOrderBy(itemType: String) = when (itemType) {
    "characters" -> OrderBy.NAME
    "comics" -> OrderBy.TITLE
    "creators" -> OrderBy.LAST_NAME
    "events" -> OrderBy.NAME
    "series" -> OrderBy.TITLE
    "stories" -> OrderBy.MODIFIED_DESC
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}