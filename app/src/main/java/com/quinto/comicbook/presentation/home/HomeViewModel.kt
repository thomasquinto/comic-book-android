package com.quinto.comicbook.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinto.comicbook.domain.repository.ComicBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ComicBookRepository
)  : ViewModel(){

    var state by mutableStateOf(HomeViewState())

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            HomeViewEvent.Refresh -> {
                viewModelScope.launch {
                    repository.deleteCache()
                    state = state.copy(
                        isRefreshing = !state.isRefreshing,
                    )
                }
            }
        }
    }
}