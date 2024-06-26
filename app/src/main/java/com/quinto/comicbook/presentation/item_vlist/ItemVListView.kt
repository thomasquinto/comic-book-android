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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.quinto.comicbook.domain.repository.getItemTypesForSearching
import com.quinto.comicbook.domain.repository.getItemTypesForSorting
import com.quinto.comicbook.domain.repository.getOrderByName
import com.quinto.comicbook.domain.repository.getOrderByValues
import com.quinto.comicbook.presentation.item_hlist.ItemLabel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemVListView(
    itemType: String,
    itemSelected: ((Item) -> Unit)? = null,
    backClicked: (() -> Unit)? = null,
    detailItem: Item? = null
) {
    val sizeFactor = if (isTablet()) 1.0f else 0.6875f

    val viewModel: ItemVListViewModel =
        hiltViewModel<ItemVListViewModel, ItemVListViewModel.ItemVListViewModelFactory>(key = itemType) { factory ->
            factory.create(itemType, detailItem)
        }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = false
    )

    if (itemType == ItemType.FAVORITE.typeName) {
        LaunchedEffect(Unit) {
            viewModel.onEvent(ItemVListEvent.LoadInitial)
        }
    }

    val useGrid = true
    val lazyListState = viewModel.lazyListState
    val lazyGridState = viewModel.lazyGridState

    // observe list scrolling
    if (useGrid) {
        val reachedBottom: Boolean by remember {
            derivedStateOf {
                val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != 0 && lastVisibleItem?.index == lazyGridState.layoutInfo.totalItemsCount - 1
            }
        }
        // load more if scrolled to bottom
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) viewModel.onEvent(ItemVListEvent.LoadMore)
        }
    } else {
        val reachedBottom: Boolean by remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != 0 && lastVisibleItem?.index == lazyListState.layoutInfo.totalItemsCount - 1
            }
        }
        // load more if scrolled to bottom
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) viewModel.onEvent(ItemVListEvent.LoadMore)
        }
    }


    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isSearchExpanded by remember { mutableStateOf(viewModel.state.searchText.isNotEmpty()) }
    val focusRequester = remember { FocusRequester() }
    var requestFocus by remember { mutableStateOf(false) }

    val searchQuery = remember { mutableStateOf(viewModel.state.searchText) }
    val coroutineScope = rememberCoroutineScope()

    val title = if (detailItem != null) {
        itemType.replaceFirstChar { it.uppercase(Locale.getDefault()) } + " - " + detailItem.name
    } else {
        itemType.replaceFirstChar { it.uppercase(Locale.getDefault()) }
    }

    Scaffold(
        topBar = {
            if (getItemTypesForSearching().contains(ItemType.byName(itemType))) {
                TopAppBar(
                    title = {
                        if (isSearchExpanded) {
                            OutlinedTextField(
                                modifier = Modifier.focusRequester(focusRequester),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
                                ),
                                value = searchQuery.value,
                                onValueChange = {newValue ->
                                    coroutineScope.launch {
                                        delay(500) // delay for 0.5 seconds
                                        if (newValue == searchQuery.value) {
                                            viewModel.onEvent(ItemVListEvent.OnSearchQueryChange(newValue))
                                        }
                                    }
                                    searchQuery.value = newValue
                                },
                                placeholder = {
                                    Text(text = "Search")
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                singleLine = true
                            )

                            if (requestFocus) {
                                LaunchedEffect(Unit) {
                                    focusRequester.requestFocus()
                                    requestFocus = false // Reset to avoid requesting focus again in future
                                }
                            }
                        } else {
                            Text(
                                text = title,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    navigationIcon =  {
                        IconButton(onClick = { backClicked?.invoke() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        if (getItemTypesForSorting().contains(ItemType.byName(itemType))) {
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

                        if (isSearchExpanded) {
                            IconButton(onClick = {
                                isSearchExpanded = false
                                viewModel.onEvent(ItemVListEvent.OnSearchQueryChange(""))
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        } else {
                            IconButton(onClick = {
                                isSearchExpanded = true
                                requestFocus = true
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { backClicked?.invoke() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.onEvent(ItemVListEvent.Refresh)
                    }
                ) {
                    if (useGrid) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed((LocalConfiguration.current.screenWidthDp / ((160 * sizeFactor)  + 10)).toInt()),
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = lazyGridState
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
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = lazyListState
                        ) {
                            items(viewModel.state.items.size) {
                                ItemLabelRow(
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
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
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
                                isSelected = viewModel.state.orderBy == orderBy
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
fun ItemLabelRow(
    item: Item,
    itemSelected: ((Item) -> Unit)? = null,
    sizeFactor: Float = 1.0f
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
                .data(item.imageUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .width((160 * sizeFactor).dp)
                .height((240 * sizeFactor).dp)
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
            modifier = Modifier
                .weight(1.0f)
        )
        Icon(
            modifier = Modifier
                .padding(end = 4.dp),
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Chevron Icon"
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
            .padding(vertical = 16.dp)
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

@Composable
fun isTablet() : Boolean {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val isTablet = screenWidthDp >= 600
    return isTablet
}