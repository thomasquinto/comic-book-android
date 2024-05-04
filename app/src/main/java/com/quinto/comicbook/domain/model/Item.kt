package com.quinto.comicbook.domain.model

import java.util.Date

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val date: Date,
    val thumbnailUrl: String,
    val itemType: ItemType
)

enum class ItemType(val typeName: String) {
    CHARACTER("characters"),
    COMIC("comics"),
    CREATOR("creators"),
    EVENT("events"),
    SERIES("series"),
    STORY("stories");

    companion object {
        fun byName(input: String): ItemType? {
            return values().firstOrNull { it.name.equals(input, true) }
        }
    }
}
