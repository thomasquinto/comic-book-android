package com.quinto.comicbook.presentation.item_list

import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.OrderBy

data class ItemListState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    var offset: Int = 0,
    var limit: Int = 20,
    var orderBy: OrderBy = OrderBy.TITLE,
    val searchText: String = "",
)