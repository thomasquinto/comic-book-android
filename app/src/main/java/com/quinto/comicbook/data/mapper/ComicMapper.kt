package com.quinto.comicbook.data.mapper

import com.quinto.comicbook.data.local.ComicEntity
import com.quinto.comicbook.data.repository.dto.ComicDto
import com.quinto.comicbook.domain.model.Comic
import com.quinto.comicbook.domain.model.OrderBy
import java.util.Date

fun ComicEntity.toComic(): Comic {
    return Comic(
        id = id ?: 0,
        title = title ?: "",
        issueNumber = issueNumber ?: 0.0,
        description = description ?: "",
        thumbnailUrl = thumbnailUrl ?: "",
        modified = modified ?: Date(),
        focDate = focDate ?: Date(),
        onSaleDate = onSaleDate ?: Date()
    )
}

fun Comic.toComicEntity(): ComicEntity {
    return ComicEntity(
        id = id,
        title = title,
        issueNumber = issueNumber,
        description = description,
        thumbnailUrl = thumbnailUrl,
        modified = modified,
        focDate = focDate,
        onSaleDate = onSaleDate
    )
}

fun ComicDto.toComic(): Comic {
    val thumbnailUrl = "${thumbnail?.path}.${thumbnail?.extension}"
        .replace("http://", "https://" )
    val focDate = dates?.find { it.type == "focDate" }?.date
    val onSaleDate = dates?.find { it.type == "onsaleDate" }?.date

    val comic = Comic(
        id = id ?: 0,
        title = title ?: "",
        issueNumber = issueNumber ?: 0.0,
        description = description ?: "",
        thumbnailUrl = thumbnailUrl,
        modified = modified ?: Date(),
        focDate = focDate,
        onSaleDate = onSaleDate
    )
    println("Comic: $comic")
    return comic
}

fun toDbOrderBy(orderBy: OrderBy): String {
    return when (orderBy) {
        OrderBy.FOC_DATE -> "focDate ASC"
        OrderBy.FOC_DATE_DESC -> "focDate DESC"
        OrderBy.ONSALE_DATE -> "onSaleDate ASC"
        OrderBy.ONSALE_DATE_DESC -> "onSaleDate DESC"
        OrderBy.TITLE -> "title ASC"
        OrderBy.TITLE_DESC -> "title DESC"
        OrderBy.ISSUE_NUMBER -> "issueNumber ASC"
        OrderBy.ISSUE_NUMBER_DESC -> "issueNumber DESC"
        OrderBy.MODIFIED -> "modified ASC"
        OrderBy.MODIFIED_DESC -> "modified DESC"
    }
}