package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Marvel Comics",
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }, // Set the navigation title here
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                item {
                    val comicViewModel: ComicViewModel =
                        hiltViewModel<ComicViewModel, ComicViewModel.ComicViewModelFactory>() { factory ->
                            factory.create(viewModel::getComics, "Comics")
                        }
                    ItemHListView(comicViewModel)
                }

                item {
                    val characterViewModel: CharacterViewModel =
                        hiltViewModel<CharacterViewModel, CharacterViewModel.CharacterViewModelFactory>() { factory ->
                            factory.create(viewModel::getCharacters, "Characters")
                        }
                    ItemHListView(characterViewModel)
                }

                item {
                    val seriesViewModel: SeriesViewModel =
                        hiltViewModel<SeriesViewModel, SeriesViewModel.SeriesViewModelFactory>() { factory ->
                            factory.create(viewModel::getSeries, "Series")
                        }
                    ItemHListView(seriesViewModel)
                }

                item {
                    val creatorViewModel: CreatorViewModel =
                        hiltViewModel<CreatorViewModel, CreatorViewModel.CreatorViewModelFactory>() { factory ->
                            factory.create(viewModel::getCreators, "Creators")
                        }
                    ItemHListView(creatorViewModel)
                }

                item {
                    val eventViewModel: EventViewModel =
                        hiltViewModel<EventViewModel, EventViewModel.EventViewModelFactory>() { factory ->
                            factory.create(viewModel::getEvents, "Events")
                        }
                    ItemHListView(eventViewModel)
                }

                item {
                    val storyViewModel: StoryViewModel =
                        hiltViewModel<StoryViewModel, StoryViewModel.StoryViewModelFactory>() { factory ->
                            factory.create(viewModel::getStories, "Stories")
                        }
                    ItemHListView(storyViewModel)
                }
            }
        }
    )

    /*
    Column {
        val itemVListViewModel: ItemVListViewModel = hiltViewModel<ItemVListViewModel, ItemVListViewModel.ItemListViewModelFactory>() { factory ->
            factory.create(viewModel::getCreators)
        }
        ItemVListView(itemVListViewModel)
    }
     */
}

@HiltViewModel(assistedFactory = CharacterViewModel.CharacterViewModelFactory::class)
class CharacterViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface CharacterViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): CharacterViewModel
    }
}

@HiltViewModel(assistedFactory = ComicViewModel.ComicViewModelFactory::class)
class ComicViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface ComicViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): ComicViewModel
    }
}


@HiltViewModel(assistedFactory = CreatorViewModel.CreatorViewModelFactory::class)
class CreatorViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface CreatorViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): CreatorViewModel
    }
}

@HiltViewModel(assistedFactory = EventViewModel.EventViewModelFactory::class)
class EventViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface EventViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): EventViewModel
    }
}

@HiltViewModel(assistedFactory = SeriesViewModel.SeriesViewModelFactory::class)
class SeriesViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface SeriesViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): SeriesViewModel
    }
}

@HiltViewModel(assistedFactory = StoryViewModel.StoryViewModelFactory::class)
class StoryViewModel @AssistedInject constructor(
    @Assisted private val getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
    @Assisted override val title: String
) : com.quinto.comicbook.presentation.item_hlist.ItemHListViewModel(getItems, title) {
    @AssistedFactory
    interface StoryViewModelFactory {
        fun create(
            getItems: suspend (Int, Int, OrderBy, String, Boolean) -> Flow<Resource<List<Item>>>,
            title: String
        ): StoryViewModel
    }
}
