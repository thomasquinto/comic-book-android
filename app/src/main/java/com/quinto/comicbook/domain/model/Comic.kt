package com.quinto.comicbook.domain.model

import java.util.Date

data class Comic(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val modified: Date
)
