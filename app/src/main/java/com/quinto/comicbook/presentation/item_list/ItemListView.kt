package com.quinto.comicbook.presentation.item_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.quinto.comicbook.domain.model.Item

@Composable
fun ItemListView(
    viewModel: ItemListViewModel
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state

    val listState = rememberLazyListState()

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 5
        }
    }
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) viewModel.onEvent(ItemListEvent.LoadMore)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchText,
            onValueChange = {
                viewModel.onEvent(
                    ItemListEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search")
            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(ItemListEvent.Refresh)
            }
        ) {
            LazyColumn (
                modifier = Modifier.fillMaxWidth(),
                state = listState
            ) {
                items(state.items.size) {
                    ItemLabel(
                        item = state.items[it],
                        modifier = Modifier
                    )
                }
                if (viewModel.state.isLoading) {
                    item {
                        LoadingItem()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemLabel(
    item: Item,
    modifier: Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = item.name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Visible,
            maxLines = 3,
        )
    }
}

@Composable
fun LoadingItem() {
    val strokeWidth = 4.dp

    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1.0f))
        CircularProgressIndicator(
            modifier = Modifier.drawBehind {
                drawCircle(
                    color = Color.Transparent,
                    radius = size.width / 2 - strokeWidth.toPx() / 2,
                    style = Stroke(strokeWidth.toPx())
                )
            },
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = strokeWidth
        )
        Spacer(modifier = Modifier.weight(1.0f))
    }
}