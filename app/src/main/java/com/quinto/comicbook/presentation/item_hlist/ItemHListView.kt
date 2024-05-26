package com.quinto.comicbook.presentation.item_hlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.presentation.item_vlist.isTablet

@Composable
fun ItemHListView(
    itemType: String,
    itemTypeSelected: ((String, Item?) -> Unit)? = null,
    itemSelected: ((Item) -> Unit)? = null,
    detailItem: Item? = null
) {
    val sizeFactor = if (isTablet()) 1.0f else 0.6875f

    val viewModel: ItemHListViewModel =
        hiltViewModel<ItemHListViewModel, ItemHListViewModel.ItemHListViewModelFactory>(key = itemType) { factory ->
            factory.create(itemType, detailItem)
        }

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
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) viewModel.onEvent(ItemHListEvent.LoadMore)
    }

    if (viewModel.state.items.isNotEmpty()) {
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
                        top = 16.dp,
                        bottom = 4.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = viewModel.itemType.replaceFirstChar(Char::uppercase),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.weight(1.0f))
                if (itemTypeSelected != null) {
                    Text(
                        text = "See all",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Chevron Icon"
                    )
                }
            }
            LazyRow(
                state = listState
            ) {
                items(viewModel.state.items.size) {
                    ItemLabel(
                        item = viewModel.state.items[it],
                        itemSelected = itemSelected,
                        sizeFactor = sizeFactor
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
    itemSelected: ((Item) -> Unit)? = null,
    sizeFactor: Float = 1.0f
) {
    Column(
        modifier = Modifier
            .width((160 * sizeFactor).dp)
            .padding(top = 8.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
            .clickable(enabled = itemSelected != null) {
                if (itemSelected != null) {
                    itemSelected(item)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .width((160 * sizeFactor).dp)
                .height((240 * sizeFactor).dp)
                .clickable(enabled = itemSelected != null) {
                    if (itemSelected != null) {
                        itemSelected(item)
                    }
                }
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier.width((160 * sizeFactor).dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = item.name,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
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
