package com.quinto.comicbook.presentation.home

import com.quinto.comicbook.domain.model.Comic
import com.quinto.comicbook.domain.model.OrderBy

data class ComicListState(
    val comics: List<Comic> = emptyList(),
    val isLoading: Boolean = false,
    var offset: Int = 0,
    var limit: Int = 20,
    var orderBy: OrderBy = OrderBy.MODIFIED_DESC,
    val titleStartsWith: String = "",
)