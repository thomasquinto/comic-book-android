package com.quinto.comicbook.data.repository.dto

import java.util.Date

@Suppress("SpellCheckingInspection")

data class ComicDto(
    val id: Int?,
    val title: String?,
    val description: String?,
    val modified: Date?,
    val thumbnail: ThumbnailDto?,
    val dates: List<DateDto>? = null,
)

