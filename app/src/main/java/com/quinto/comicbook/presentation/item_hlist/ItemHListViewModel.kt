package com.quinto.comicbook.presentation.item_hlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ItemHListViewModel.ItemHListViewModelFactory::class)
open class ItemHListViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted open val title: String
) : ViewModel() {

    @AssistedFactory
    interface ItemHListViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): ItemHListViewModel
    }

    var state by mutableStateOf(ItemHListState())

    init {
        getItems()
    }

    fun onEvent(event: ItemHListEvent) {
        when (event) {
            is ItemHListEvent.LoadMore -> {
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
            getItems(state.offset, state.limit, state.orderBy, "", false)
                .collect { result ->
                    when (result) {
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