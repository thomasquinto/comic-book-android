package com.quinto.comicbook.domain.model

import java.util.Date

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val date: Date,
    val thumbnailUrl: String,
)

enum class OrderBy(val value: String) {
    TITLE("title"),
    TITLE_DESC("-title"),
    MODIFIED("modified"),
    MODIFIED_DESC("-modified"),
}
