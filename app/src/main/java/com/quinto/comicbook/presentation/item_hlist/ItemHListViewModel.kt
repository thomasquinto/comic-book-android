package com.quinto.comicbook.presentation.item_hlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.domain.repository.getDefaultOrderBy
import com.quinto.comicbook.domain.repository.getFetchDetails
import com.quinto.comicbook.domain.repository.getFetchItems
import com.quinto.comicbook.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ItemHListViewModel.ItemHListViewModelFactory::class)
open class ItemHListViewModel @AssistedInject constructor(
    @Assisted val itemType: String,
    @Assisted val detailItem: Item? = null,
    private val repository: ComicBookRepository
) : ViewModel() {

    @AssistedFactory
    interface ItemHListViewModelFactory {
        fun create(
            itemType: String,
           detailItem: Item? = null
        ): ItemHListViewModel
    }

    var state by mutableStateOf(ItemHListState())

    init {
        getItems()
    }

    fun onEvent(event: ItemHListEvent) {
        when (event) {
            is ItemHListEvent.LoadMore -> {
                getItems()
            }
        }
    }

    private fun getItems() {
        viewModelScope.launch {

            val responseFlow: Flow<Resource<List<Item>>> = if (detailItem != null) {
                getFetchDetails(itemType, repository)(
                    detailItem.itemType.typeName,
                    detailItem.id,
                    state.offset,
                    state.limit,
                    getDefaultOrderBy(itemType),
                    false
                )
            } else {
                getFetchItems(itemType, repository)(
                    state.offset,
                    state.limit,
                    state.orderBy,
                    "",
                    false
                )
            }

            responseFlow .collect { result ->
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