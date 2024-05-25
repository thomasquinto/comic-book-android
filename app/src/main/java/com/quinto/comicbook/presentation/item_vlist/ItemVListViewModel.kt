package com.quinto.comicbook.presentation.item_vlist

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.domain.repository.getDefaultOrderBy
import com.quinto.comicbook.domain.repository.getFetchItems
import com.quinto.comicbook.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ItemVListViewModel.ItemVListViewModelFactory::class)
class ItemVListViewModel @AssistedInject constructor (
    @Assisted val itemType: String,
    @Assisted val detailItem: Item? = null,
    private val repository: ComicBookRepository
): ViewModel() {

    @AssistedFactory
    interface ItemVListViewModelFactory {
        fun create(
            itemType: String,
            detailItem: Item? = null
        ): ItemVListViewModel
    }

    var state by mutableStateOf(ItemVListState())
    var lazyListState by mutableStateOf(LazyListState())
    var lazyGridState by mutableStateOf(LazyGridState())

    private var searchItems: Job? = null

    init {
        if (itemType != ItemType.FAVORITE.typeName) {
            state.orderBy = getDefaultOrderBy(itemType)
            getItems(true)
        }
    }

    fun onEvent(event: ItemVListEvent) {
        when(event) {
            is ItemVListEvent.LoadInitial -> {
                getItems(true)
            }
            is ItemVListEvent.Refresh -> {
                getItems(reset = true, fetchFromRemote = true)
            }
            is ItemVListEvent.OnSearchQueryChange -> {
                state = state.copy(searchText = event.query)
                searchItems?.cancel()
                searchItems = viewModelScope.launch {
                    delay(500L)
                    getItems(reset = true)
                }
            }
            is ItemVListEvent.SortBy -> {
                state = state.copy(orderBy = event.orderBy)
                getItems(reset = true, fetchFromRemote = false)
            }
            is ItemVListEvent.LoadMore -> {
                getItems(reset = false)
            }
        }
    }

    private fun getItems(
        reset: Boolean = true,
        fetchFromRemote: Boolean = false
    ) {
        if(itemType == ItemType.FAVORITE.typeName) {
            if (reset) {
                viewModelScope.launch {
                    repository.retrieveFavoriteItems().let {
                        state = state.copy(
                            items = it
                        )
                    }
                }
            }
            return
        }

        if (reset) {
            state.offset = 0
        } else {
            state.offset += state.limit
        }
        viewModelScope.launch {
            getFetchItems(itemType, repository)(
                detailItem?.itemType?.typeName ?: "",
                detailItem?.id ?: 0,
                state.offset,
                state.limit,
                state.orderBy,
                state.searchText,
                fetchFromRemote
            )
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = if (state.offset == 0) {
                                    state.copy(
                                        items = it
                                    )
                                } else {
                                    state.copy(
                                        items = state.items + it
                                    )
                                }
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }

            if (reset) {
                lazyListState.scrollToItem(0)
                lazyGridState.scrollToItem(0)
            }
        }
    }

}