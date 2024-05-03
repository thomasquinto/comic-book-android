package com.quinto.comicbook.presentation.item_vlist

import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.data.remote.OrderBy

data class ItemVListState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    var offset: Int = 0,
    var limit: Int = 20,
    var orderBy: OrderBy = OrderBy.LAST_NAME,
    val searchText: String = "",
)