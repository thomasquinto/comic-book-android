package com.quinto.comicbook.presentation.home

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
                getComics(fetchFromRemote = true)
            }
            is ComicListEvent.OnSearchQueryChange -> {
                state = state.copy(titleStartsWith = event.query)
                searchComics?.cancel()
                searchComics = viewModelScope.launch {
                    delay(500L)
                    getComics()
                }
            }
        }
    }


    private fun getComics(
        query: String = state.titleStartsWith.lowercase(),
        fetchFromRemote: Boolean = true
    ) {
        viewModelScope.launch {
            repository
                .getComics(fetchFromRemote, state.offset, state.limit, state.orderBy, state.titleStartsWith)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(
                                    comics = it
                                )
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