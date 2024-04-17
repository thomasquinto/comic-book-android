package com.quinto.comicbook.data.repository.dto

import com.squareup.moshi.Json

data class ComicDto(
    val id: Int?,
    val title: String?,
    val description: String?,
    val thumbnail: ThumbnailDto?,
    //val modified: Date?,
)

data class ThumbnailDto(
    val path: String?,
    val extension: String?
)
