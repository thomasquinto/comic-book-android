package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.presentation.item_hlist.ItemHListView
import com.quinto.comicbook.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel()
) {
    Column {
        val comicViewModel: ComicViewModel =
            hiltViewModel<ComicViewModel, ComicViewModel.ComicViewModelFactory>() { factory ->
                factory.create(viewModel::getComics)
            }
        ItemHListView(comicViewModel)

        val characterViewModel: CharacterViewModel =
            hiltViewModel<CharacterViewModel, CharacterViewModel.CharacterViewModelFactory>() { factory ->
                factory.create(viewModel::getCharacters)
            }
        ItemHListView(characterViewModel)
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

@HiltViewModel(assistedFactory = ComicViewModel.ComicViewModelFactory::class)
open class ComicViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems) {
    @AssistedFactory
    interface ComicViewModelFactory {
        fun create(getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>): ComicViewModel
    }
}

@HiltViewModel(assistedFactory = CharacterViewModel.CharacterViewModelFactory::class)
open class CharacterViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems) {
    @AssistedFactory
    interface CharacterViewModelFactory {
        fun create(getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>): CharacterViewModel
    }
}
