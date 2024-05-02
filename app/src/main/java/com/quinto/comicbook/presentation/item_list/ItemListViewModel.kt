package com.quinto.comicbook.presentation.item_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val repository: ComicBookRepository
): ViewModel() {

    var state by mutableStateOf(ItemListState())

    private var searchItems: Job? = null

    init {
        getItems()
    }

    fun onEvent(event: ItemListEvent) {
        when(event) {
            is ItemListEvent.Refresh -> {
                getItems(reset = true)
            }
            is ItemListEvent.OnSearchQueryChange -> {
                state = state.copy(searchText = event.query)
                searchItems?.cancel()
                searchItems = viewModelScope.launch {
                    delay(500L)
                    getItems(reset = true)
                }
            }
            is ItemListEvent.LoadMore -> {
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
            repository
                .getComics(state.offset, state.limit, state.orderBy, state.searchText, false)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                if (state.offset == 0) {
                                    state = state.copy(
                                        items = it
                                    )
                                } else {
                                    state = state.copy(
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