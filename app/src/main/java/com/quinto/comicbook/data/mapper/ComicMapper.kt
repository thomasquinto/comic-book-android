package com.quinto.comicbook.data.mapper

import com.quinto.comicbook.data.local.ComicEntity
import com.quinto.comicbook.data.repository.dto.ComicDto
import com.quinto.comicbook.domain.model.Comic
import java.util.Date

fun ComicEntity.toComic(): Comic {
    return Comic(
        id = id ?: 0,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        modified = modified
    )
}

fun Comic.toComicEntity(): ComicEntity {
    return ComicEntity(
        id = id,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        modified = modified
    )
}

fun ComicDto.toComic(): Comic {
    val comic = Comic(
        id = id ?: 0,
        title = title ?: "",
        description = description ?: "",
        thumbnailUrl = "${thumbnail?.path}.${thumbnail?.extension}".replace("http://", "https://" ),
        //modified = modified ?: Date()
        modified = Date()
    )
    println("Comic: $comic")
    return comic
}