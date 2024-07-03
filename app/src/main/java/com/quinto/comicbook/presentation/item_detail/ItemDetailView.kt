package com.quinto.comicbook.presentation.item_detail

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.getItemTypesForDetail
import com.quinto.comicbook.presentation.item_hlist.ItemHListView
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailView(
    item: Item,
    itemSelected: ((Item) -> Unit)? = null,
    itemTypeSelected: ((String, Item?) -> Unit)? = null,
    backClicked: (() -> Unit)? = null
    ) {

    val viewModel: ItemDetailViewModel =
        hiltViewModel<ItemDetailViewModel, ItemDetailViewModel.ItemDetailViewModelFactory>() { factory ->
            factory.create(item)
        }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.cancelCoroutine()
        }
    }

    Box {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ImageLoaderWithBitmap(
                        imageUrl = item.imageUrl,
                        viewModel = viewModel
                    )
                    /*
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            //.heightIn(max = (max(LocalConfiguration.current.screenHeightDp, LocalConfiguration.current.screenWidthDp) / 1.3).dp)
                    )
                     */

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                            .alpha(0.8f)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = CircleShape
                                )
                        ) {
                            IconButton(onClick = {
                                viewModel.onEvent(
                                    ItemDetailViewEvent.Favorite(item)
                                )
                            }) {
                                if (viewModel.state.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = "Favorite",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                    Text(
                        text = item.name,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    if (item.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.description,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    if(viewModel.state.contentDescription.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.state.contentDescription,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }

            getItemTypesForDetail(item.itemType.typeName).forEach { itemType ->
                item {
                    ItemHListView(
                        itemType = itemType.typeName,
                        itemSelected = itemSelected,
                        itemTypeSelected = itemTypeSelected,
                        detailItem = item
                    )
                }
            }
        }

        TopAppBar(
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor= Color.Transparent,
                navigationIconContentColor= Color.Transparent,
                titleContentColor= Color.Transparent,
                actionIconContentColor= Color.Transparent,
            ),
            title = {
                Text(
                    text = "",//item.itemType.typeName.replaceFirstChar { it.uppercase(Locale.getDefault()) },
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .alpha(0.8f)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = CircleShape
                            )
                    ) {
                        IconButton(onClick = {
                            backClicked?.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        )

    }


}

@Composable
fun ImageLoaderWithBitmap(imageUrl: String, viewModel: ItemDetailViewModel) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(coil.size.Size.ORIGINAL)
            .allowHardware(false)
            .build(),
        onSuccess = { state ->
            if (state.result is SuccessResult) {
                val bitmap = (state.result.drawable as BitmapDrawable).bitmap
                viewModel.onDetailImageLoaded(bitmap);
            }
        }
    )

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ItemDetailViewPreview() {
    ItemDetailView(
        Item(
            id = 71400,
            name = "Superior Spider-Man Vol. 2: Otto-matic (Trade Paperback)",
            description = "Collects Superior Spider-Man (2018) #7-12.  Like the rest of America, the West Coast has been overrun with Frost Giants! But Otto Octavius doesn’t settle for chaos — he plans to win the War of the Realms single-handedly. Well, maybe not single-handedly. He needs minions. Super-minions! So he recruits…the West Coast Avengers?! Will Otto Octavius and Quentin Quire become BFFs? If they can somehow work together and save San Francisco, Otto will be due a parade — but any celebrations might be short-lived, because someone dangerous is coming. Otto calls himself the Superior Spider-Man? Norman Osborn, the Spider-Man of Earth-44145 you met in SPIDER-GEDDON, begs to differ — and has some very creative ways to prove his true superiority! Norman is out to destroy everything. Does Otto stand a chance of stopping him? Does he even stand a chance at living through this?!",
            date = Date(),
            imageUrl = "https://i.annihil.us/u/prod/marvel/i/mg/c/f0/5df3fc8b3c883.jpg",
            itemType = ItemType.COMIC
        )
    )
}