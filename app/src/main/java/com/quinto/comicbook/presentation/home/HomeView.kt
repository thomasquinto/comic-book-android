package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quinto.comicbook.presentation.item_list.ItemListView
import com.quinto.comicbook.presentation.item_list.ItemListViewModel

@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel()
) {
    Column {
        val itemListViewModel: ItemListViewModel = hiltViewModel<ItemListViewModel, ItemListViewModel.ItemListViewModelFactory>() { factory ->
            factory.create(viewModel::getComics)
        }
        ItemListView(itemListViewModel)
    }
}

