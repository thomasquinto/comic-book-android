package com.quinto.comicbook.presentation.comic_list

sealed class ComicListEvent {
    data object Refresh: ComicListEvent()
    data class OnSearchQueryChange(val query: String): ComicListEvent()
    data object LoadMore: ComicListEvent()
}