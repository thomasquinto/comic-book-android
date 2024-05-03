package com.quinto.comicbook.domain.model

import java.util.Date

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val date: Date,
    val thumbnailUrl: String,
)

