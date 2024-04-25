package com.quinto.comicbook.presentation.comic_list

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
class ComicListViewModel @Inject constructor(
    private val repository: ComicBookRepository
): ViewModel() {

    var state by mutableStateOf(ComicListState())

    private var searchComics: Job? = null

    init {
        getComics()
    }

    fun onEvent(event: ComicListEvent) {
        when(event) {
            is ComicListEvent.Refresh -> {
                getComics(reset = true)
            }
            is ComicListEvent.OnSearchQueryChange -> {
                state = state.copy(searchText = event.query)
                searchComics?.cancel()
                searchComics = viewModelScope.launch {
                    delay(500L)
                    getComics(reset = true)
                }
            }
            is ComicListEvent.LoadMore -> {
                getComics(reset = false)
            }
        }
    }

    private fun getComics(
        reset: Boolean = true
    ) {
        if (reset) {
            state.offset = 0
        } else {
            state.offset += state.limit
        }
        viewModelScope.launch {
            repository
                .getComics(false, state.offset, state.limit, state.orderBy, state.searchText)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                if (state.offset == 0) {
                                    state = state.copy(
                                        comics = it
                                    )
                                } else {
                                    state = state.copy(
                                        comics = state.comics + it
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