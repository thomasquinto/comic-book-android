package com.quinto.comicbook.presentation.home

sealed class HomeViewEvent {
    data object Refresh : HomeViewEvent()
}