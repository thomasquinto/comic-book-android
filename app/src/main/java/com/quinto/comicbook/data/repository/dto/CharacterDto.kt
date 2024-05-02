package com.quinto.comicbook.data.repository.dto

import com.quinto.comicbook.domain.model.Item
import java.util.Date

data class CharacterDto(
    val id: Int?,
    val name: String?,
    val description: String?,
    val modified: Date?,
    val thumbnail: ThumbnailDto?,
    val dates: List<DateDto>? = null,
) : MappedItem {
    override fun toItem(): Item {
        val item = Item(
            id = id ?: 0,
            name = name ?: "",
            description = description ?: "",
            thumbnailUrl = thumbnail?.thumbnailUrl ?: "",
            date = modified ?: Date(),
        )
        println("Item: $item")
        return item
    }
}

