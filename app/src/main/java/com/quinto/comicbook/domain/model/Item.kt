package com.quinto.comicbook.domain.model

import java.util.Date

data class Item(
    val id: Int,
    val itemType: ItemType,
    val name: String,
    val description: String,
    val date: Date,
    val imageUrl: String,
)

enum class ItemType(val typeName: String) {
    CHARACTER("characters"),
    COMIC("comics"),
    CREATOR("creators"),
    EVENT("events"),
    SERIES("series"),
    STORY("stories"),
    FAVORITE("favorites");

    companion object {
        fun byName(input: String): ItemType? {
            return entries.firstOrNull { it.typeName.equals(input, true) }
        }
    }
}
