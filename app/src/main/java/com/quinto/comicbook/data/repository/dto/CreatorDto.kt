package com.quinto.comicbook.data.repository.dto

import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import java.util.Date

data class CreatorDto(
    val id: Int?,
    val fullName: String?,
    val description: String?,
    val modified: Date?,
    val thumbnail: ThumbnailDto?,
    val dates: List<DateDto>? = null,
) : MappedItem {
    override fun toItem(): Item {
        val item = Item(
            id = id ?: 0,
            name = fullName ?: "",
            description = description ?: "",
            thumbnailUrl = thumbnail?.thumbnailUrl ?: ComicBookApi.IMAGE_MISSING_URL,
            date = modified ?: Date(),
            itemType = ItemType.CREATOR
        )
        println("Item: $item")
        return item
    }
}

