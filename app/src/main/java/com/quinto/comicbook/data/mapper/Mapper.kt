package com.quinto.comicbook.data.mapper

import com.quinto.comicbook.data.local.ItemEntity
import com.quinto.comicbook.domain.model.Item
import java.util.Date

fun ItemEntity.toItem(): Item {
    return Item(
        id = id ?: 0,
        name = name ?: "",
        description = description ?: "",
        thumbnailUrl = thumbnailUrl ?: "",
        date = date ?: Date(),
    )
}

fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        name = name,
        description = description,
        thumbnailUrl = thumbnailUrl,
        date = date,
    )
}
