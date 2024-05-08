package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.repository.getItemTypesForHome
import com.quinto.comicbook.presentation.item_hlist.ItemHListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    itemTypeSelected: ((String) -> Unit)? = null,
    itemSelected: ((Item) -> Unit)? = null
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = false
    )

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
                }
            )
        },
        content = { paddingValues ->
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(HomeViewEvent.Refresh)
                }
            ) {
                key(viewModel.state.isRefreshing) {
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        getItemTypesForHome().forEach { itemType ->
                            item {
                                ItemHListView(itemType.typeName, itemTypeSelected, itemSelected)
                            }
                        }
                    }
                }
            }
        }
    )
}
