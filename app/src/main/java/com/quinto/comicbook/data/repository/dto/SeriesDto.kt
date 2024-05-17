package com.quinto.comicbook.data.repository.dto

import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import java.util.Date

data class SeriesDto(
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
            imageUrl = thumbnail?.thumbnailUrl ?: ComicBookApi.IMAGE_MISSING_URL,
            date = modified ?: Date(),
            itemType = ItemType.SERIES
        )
        println("Item: $item")
        return item
    }
}

