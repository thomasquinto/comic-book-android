package com.quinto.comicbook.presentation.item_hlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType

@Composable
fun ItemHListView(
    itemType: String,
    itemTypeSelected: ((String, Item?) -> Unit)? = null,
    itemSelected: ((Item) -> Unit)? = null,
    detailItem: Item? = null
) {
    val viewModel: ItemHListViewModel =
        hiltViewModel<ItemHListViewModel, ItemHListViewModel.ItemHListViewModelFactory>(key = itemType) { factory ->
            factory.create(itemType, detailItem)
        }

    val state = viewModel.state

    val listState = rememberLazyListState()

    if (itemType == ItemType.FAVORITE.typeName) {
        LaunchedEffect(Unit) {
            viewModel.onEvent(ItemHListEvent.LoadInitial)
        }
    }

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 5
        }
    }
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) viewModel.onEvent(ItemHListEvent.LoadMore)
    }

    if (state.items.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                )
        ) {

            Row(
                modifier = Modifier
                    .clickable(enabled = itemTypeSelected != null) {
                        if (itemTypeSelected != null) {
                            itemTypeSelected(itemType, detailItem)
                        }
                    }
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = viewModel.itemType.replaceFirstChar(Char::uppercase),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.weight(1.0f))
                if (itemTypeSelected != null) {
                    Text(
                        text = "See all",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            LazyRow(
                state = listState
            ) {
                items(state.items.size) {
                    ItemLabel(
                        item = state.items[it],
                        itemSelected = itemSelected
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
    itemSelected: ((Item) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .width(118.dp)
            .padding(4.dp)
            .clickable(enabled = itemSelected != null) {
                if (itemSelected != null) {
                    itemSelected(item)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .width(110.dp)
                .height(165.dp)
                .clickable(enabled = itemSelected != null) {
                    println("Item selected: $item")
                    if (itemSelected != null) {
                        itemSelected(item)
                    }
                }
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = item.name,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
fun LoadingItem() {
    val strokeWidth = 4.dp
    CircularProgressIndicator(
        modifier = Modifier
            .drawBehind {
                drawCircle(
                    color = Color.Transparent,
                    radius = size.width / 2 - strokeWidth.toPx() / 2,
                    style = Stroke(strokeWidth.toPx())
                )
            },
        color = MaterialTheme.colorScheme.background, // make loading spinner invisible
        strokeWidth = strokeWidth
    )
}
