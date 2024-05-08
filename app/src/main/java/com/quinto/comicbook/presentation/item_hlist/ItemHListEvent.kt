package com.quinto.comicbook.presentation.item_hlist

sealed class ItemHListEvent {
        data object LoadInitial: ItemHListEvent()
        data object LoadMore: ItemHListEvent()
}