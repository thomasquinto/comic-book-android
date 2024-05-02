package com.quinto.comicbook.data.repository.dto

data class ThumbnailDto(
    val path: String?,
    val extension: String?
) {
    val thumbnailUrl: String
        get() = "${path}.${extension}".replace("http://", "https://" )
}