package com.quinto.comicbook.presentation.item_vlist

import com.quinto.comicbook.data.remote.OrderBy

sealed class ItemVListEvent {
    data object LoadInitial: ItemVListEvent()

    data object Refresh: ItemVListEvent()

    data class OnSearchQueryChange(val query: String): ItemVListEvent()

    data class SortBy(val orderBy: OrderBy): ItemVListEvent()

    data object LoadMore: ItemVListEvent()
}