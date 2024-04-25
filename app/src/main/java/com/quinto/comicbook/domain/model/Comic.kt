package com.quinto.comicbook.domain.model

import java.util.Date

data class Comic(
    val id: Int,
    val title: String,
    val issueNumber: Double? = null,
    val description: String,
    val modified: Date,
    val thumbnailUrl: String,
    val focDate: Date? = null,
    val onSaleDate: Date? = null
)

enum class OrderBy(val value: String) {
    FOC_DATE("focDate"),
    FOC_DATE_DESC("-focDate"),
    ONSALE_DATE("onsaleDate"),
    ONSALE_DATE_DESC("-onsaleDate"),
    TITLE("title"),
    TITLE_DESC("-title"),
    ISSUE_NUMBER("issueNumber"),
    ISSUE_NUMBER_DESC("-issueNumber"),
    MODIFIED("modified"),
    MODIFIED_DESC("-modified"),
}
