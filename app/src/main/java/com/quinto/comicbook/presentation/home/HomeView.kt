package com.quinto.comicbook.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.quinto.comicbook.R
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.repository.getItemTypesForHome
import com.quinto.comicbook.presentation.item_hlist.ItemHListView
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    itemTypeSelected: ((String, Item?) -> Unit)? = null,
    itemSelected: ((Item) -> Unit)? = null
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = false
    )

    // Scroll state to track the scroll position
    val scrollState = rememberScrollState()

    // Content of the screen
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.onEvent(HomeViewEvent.Refresh)
        }
    ) {
        key(viewModel.state.isRefreshing) {
            Box {
                // Parallax hero image
                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Hero Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                        .graphicsLayer {
                            alpha = min(1f, 1 - (scrollState.value / 1200f))
                            translationY = -scrollState.value * 0.5f
                        }
                )

                // Scrollable content
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(top = 420.dp)
                ) {
                    getItemTypesForHome().forEach { itemType ->
                        ItemHListView(itemType.typeName, itemTypeSelected, itemSelected)
                    }
                }
            }
        }
    }

}
