package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quinto.comicbook.presentation.item_hlist.ItemHListView
import com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel

@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel()
) {
    Column {
        val comicViewModel: ItemHListViewModel = hiltViewModel<ItemHListViewModel, ItemHListViewModel.ItemListViewModelFactory>() { factory ->
            factory.create(viewModel::getComics)
        }
        ItemHListView(comicViewModel)
        /*
        val characterViewModel: ItemHListViewModel = hiltViewModel<ItemHListViewModel, ItemHListViewModel.ItemListViewModelFactory>() { factory ->
            factory.create(viewModel::getCharacters)
        }
        ItemHListView(characterViewModel)
         */
    }

    /*
    Column {
        val itemVListViewModel: ItemVListViewModel = hiltViewModel<ItemVListViewModel, ItemVListViewModel.ItemListViewModelFactory>() { factory ->
            factory.create(viewModel::getCreators)
        }
        ItemVListView(itemVListViewModel)
    }
     */
}

