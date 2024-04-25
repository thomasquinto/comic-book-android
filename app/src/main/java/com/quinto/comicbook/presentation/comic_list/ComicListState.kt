package com.quinto.comicbook.presentation.comic_list

import com.quinto.comicbook.domain.model.Comic
import com.quinto.comicbook.domain.model.OrderBy

data class ComicListState(
    val comics: List<Comic> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    var offset: Int = 0,
    var limit: Int = 20,
    var orderBy: OrderBy = OrderBy.TITLE,
    val searchText: String = "",
)