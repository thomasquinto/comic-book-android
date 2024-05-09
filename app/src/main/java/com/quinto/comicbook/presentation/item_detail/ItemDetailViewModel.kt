package com.quinto.comicbook.presentation.item_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.repository.ComicBookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ItemDetailViewModel.ItemDetailViewModelFactory::class)
class ItemDetailViewModel @AssistedInject constructor(
    @Assisted private val itemId: Int,
    private val repository: ComicBookRepository
)  : ViewModel(){

    @AssistedFactory
    interface ItemDetailViewModelFactory {
        fun create(
            itemId: Int
        ): ItemDetailViewModel
    }

    var state by mutableStateOf(ItemDetailViewState())

    init {
        viewModelScope.launch {
            repository.retrieveFavoriteItems().let { favoriteItems ->
                state = state.copy(
                    isFavorite = favoriteItems.map { it.id }.contains(itemId)
                )
            }
        }
    }

    fun onEvent(event: ItemDetailViewEvent) {
        when (event) {
            is ItemDetailViewEvent.Favorite -> {
                state = state.copy(
                    isFavorite = !state.isFavorite
                )
                viewModelScope.launch {
                    repository.updateFavorite(event.itemId, state.isFavorite)
                }
            }
        }
    }
}