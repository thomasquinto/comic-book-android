package com.quinto.comicbook.presentation.home

import com.quinto.comicbook.domain.model.Comic

data class HomeState(
    val comics: List<Comic> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)