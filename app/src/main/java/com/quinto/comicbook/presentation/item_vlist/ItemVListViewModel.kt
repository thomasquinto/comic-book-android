package com.quinto.comicbook.presentation.item_vlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.repository.ComicBookRepository
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
    @Assisted open val itemType: String,
    private val repository: ComicBookRepository
): ViewModel() {

    @AssistedFactory
    interface ItemVListViewModelFactory {
        fun create(
            itemType: String
        ): ItemVListViewModel
    }

    var state by mutableStateOf(ItemVListState())

    private var searchItems: Job? = null

    init {
        getItems()
    }

    fun onEvent(event: ItemVListEvent) {
        when(event) {
            is ItemVListEvent.Refresh -> {
                getItems(reset = true)
            }
            is ItemVListEvent.OnSearchQueryChange -> {
                state = state.copy(searchText = event.query)
                searchItems?.cancel()
                searchItems = viewModelScope.launch {
                    delay(500L)
                    getItems(reset = true)
                }
            }
            is ItemVListEvent.LoadMore -> {
                getItems(reset = false)
            }
        }
    }

    private fun getItems(
        reset: Boolean = true
    ) {
        if (reset) {
            state.offset = 0
        } else {
            state.offset += state.limit
        }
        viewModelScope.launch {
            getFetchItems(itemType, repository)(state.offset, state.limit, state.orderBy, state.searchText, false)
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
        }
    }

}