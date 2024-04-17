package com.quinto.comicbook.presentation.home

sealed class ComicListEvent {
    object Refresh: ComicListEvent()
    data class OnSearchQueryChange(val query: String): ComicListEvent()
}