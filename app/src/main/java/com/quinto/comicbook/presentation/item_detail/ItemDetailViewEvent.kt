package com.quinto.comicbook.presentation.item_detail

sealed class ItemDetailViewEvent {
    data class Favorite(val itemId: Int) : ItemDetailViewEvent()
}