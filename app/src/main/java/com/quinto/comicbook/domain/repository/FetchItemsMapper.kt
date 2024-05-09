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
    ItemType.FAVORITE,
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

fun getOrderByValues(itemType: String) = when (itemType) {
    "characters" -> listOf(OrderBy.NAME, OrderBy.MODIFIED_DESC)
    "comics" -> listOf(OrderBy.TITLE, OrderBy.ON_SALE_DATE, OrderBy.ON_SALE_DATE_DESC, OrderBy.MODIFIED_DESC)
    "creators" -> listOf(OrderBy.LAST_NAME, OrderBy.FIRST_NAME, OrderBy.MODIFIED_DESC)
    "events" -> listOf(OrderBy.NAME, OrderBy.START_DATE, OrderBy.START_DATE_DESC, OrderBy.MODIFIED_DESC)
    "series" -> listOf(OrderBy.TITLE, OrderBy.START_YEAR, OrderBy.START_YEAR_DESC, OrderBy.MODIFIED_DESC)
    "stories" -> listOf(OrderBy.MODIFIED_DESC)
    else -> throw IllegalArgumentException("Unknown item type: $itemType")
}

fun getOrderByName(orderBy: OrderBy) = when (orderBy) {
    OrderBy.NAME -> "Name"
    OrderBy.NAME_DESC -> "Name: Reverse"
    OrderBy.TITLE -> "Title"
    OrderBy.TITLE_DESC -> "Title: Reverse"
    OrderBy.ON_SALE_DATE -> "On Sale Date"
    OrderBy.ON_SALE_DATE_DESC -> "On Sale Date: Most Recent"
    OrderBy.LAST_NAME -> "Last Name"
    OrderBy.LAST_NAME_DESC -> "Last Name: Reverse"
    OrderBy.FIRST_NAME -> "First Name"
    OrderBy.FIRST_NAME_DESC -> "First Name: Reverse"
    OrderBy.START_DATE -> "Start Date"
    OrderBy.START_DATE_DESC -> "Start Date: Most Recent"
    OrderBy.START_YEAR -> "Start Year"
    OrderBy.START_YEAR_DESC -> "Start Year: Most Recent"
    OrderBy.MODIFIED -> "Modified Date"
    OrderBy.MODIFIED_DESC -> "Modified Date: Most Recent"
}