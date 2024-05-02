package com.quinto.comicbook.data.repository.dto

import com.quinto.comicbook.domain.model.Item
import java.util.Date

data class ComicDto(
    val id: Int?,
    val title: String?,
    val description: String?,
    val modified: Date?,
    val thumbnail: ThumbnailDto?,
    val dates: List<DateDto>? = null,
) : MappedItem {
    override fun toItem(): Item {
        val item = Item(
            id = id ?: 0,
            name = title ?: "",
            description = description ?: "",
            thumbnailUrl = thumbnail?.thumbnailUrl ?: "",
            date = modified ?: Date(),
        )
        println("Item: $item")
        return item
    }
}

