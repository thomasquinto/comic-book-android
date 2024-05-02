package com.quinto.comicbook.data.mapper

import com.quinto.comicbook.data.local.ItemEntity
import com.quinto.comicbook.data.repository.dto.CharacterDto
import com.quinto.comicbook.data.repository.dto.ComicDto
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.OrderBy
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

fun toDbOrderBy(orderBy: OrderBy): String {
    return when (orderBy) {
        OrderBy.NAME -> "title ASC"
        OrderBy.NAME_DESC -> "title DESC"
        OrderBy.TITLE -> "title ASC"
        OrderBy.TITLE_DESC -> "title DESC"
        OrderBy.MODIFIED -> "modified ASC"
        OrderBy.MODIFIED_DESC -> "modified DESC"
    }
}