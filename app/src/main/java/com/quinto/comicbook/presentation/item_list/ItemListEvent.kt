package com.quinto.comicbook.presentation.item_list

sealed class ItemListEvent {
    data object Refresh: ItemListEvent()
    data class OnSearchQueryChange(val query: String): ItemListEvent()
    data object LoadMore: ItemListEvent()
}