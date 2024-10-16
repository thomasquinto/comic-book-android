package com.quinto.comicbook.presentation.item_hlist

import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.data.remote.OrderBy

data class ItemHListState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    var offset: Int = 0,
    var limit: Int = 20,
    var orderBy: OrderBy = OrderBy.MODIFIED_DESC,
    val searchText: String = "",
    val hasNoMore: Boolean = false,
)