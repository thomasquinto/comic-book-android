package com.quinto.comicbook.presentation.item_vlist

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.getOrderByName
import com.quinto.comicbook.domain.repository.getOrderByValues
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemVListView(
    itemType: String,
    itemSelected: ((Item) -> Unit)? = null,
    backClicked: (() -> Unit)? = null
) {
    val viewModel: ItemVListViewModel =
        hiltViewModel<ItemVListViewModel, ItemVListViewModel.ItemVListViewModelFactory>(key = itemType) { factory ->
            factory.create(itemType)
        }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = false
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
        if (reachedBottom) viewModel.onEvent(ItemVListEvent.LoadMore)
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = itemType.replaceFirstChar { it.uppercase(Locale.getDefault()) },
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon =  {
                    IconButton(onClick = { backClicked?.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row {
                    if (itemType != ItemType.STORY.typeName) {
                        OutlinedTextField(
                            value = state.searchText,
                            onValueChange = {
                                viewModel.onEvent(
                                    ItemVListEvent.OnSearchQueryChange(it)
                                )
                            },
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 16.dp)
                                .weight(1.0f),
                            placeholder = {
                                Text(text = "Search")
                            },
                            maxLines = 1,
                            singleLine = true
                        )

                        IconButton(onClick = {
                            showBottomSheet = true
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.onEvent(ItemVListEvent.Refresh)
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
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

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    ) {
                        Text(
                            text ="Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        getOrderByValues(itemType).forEach { orderBy ->
                            SortingOption(
                                option = orderBy,
                                isSelected = state.orderBy == orderBy
                            ) {
                                viewModel.onEvent(ItemVListEvent.SortBy(orderBy))
                                showBottomSheet = false
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ItemLabel(
    item: Item,
    itemSelected: ((Item) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(enabled = itemSelected != null) {
                if (itemSelected != null) {
                    itemSelected(item)
                }
            },
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
                .width(110.dp)
                .height(130.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
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

@Composable
private fun SortingOption(
    option: OrderBy,
    isSelected: Boolean,
    onOptionSelected: (OrderBy) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .selectable(
                selected = isSelected,
                onClick = { onOptionSelected(option) }
            )
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onBackground)
        )

        Text(
            text = getOrderByName(option),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}