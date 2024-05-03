package com.quinto.comicbook.presentation.item_vlist

sealed class ItemVListEvent {
    data object Refresh: ItemVListEvent()
    data class OnSearchQueryChange(val query: String): ItemVListEvent()
    data object LoadMore: ItemVListEvent()
}