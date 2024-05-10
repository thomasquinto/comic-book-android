package com.quinto.comicbook.presentation.item_detail

import com.quinto.comicbook.domain.model.Item

sealed class ItemDetailViewEvent {
    data class Favorite(val item: Item) : ItemDetailViewEvent()
}