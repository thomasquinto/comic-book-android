package com.quinto.comicbook.presentation.item_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.getItemTypesForDetail
import com.quinto.comicbook.presentation.item_hlist.ItemHListView
import java.util.Date

@Composable
fun ItemDetailView(
    item: Item,
    itemSelected: ((Item) -> Unit)? = null,
    ) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = item.name,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.description,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    //fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        getItemTypesForDetail(item.itemType.typeName).forEach { itemType ->
            item {
                ItemHListView(
                    itemType = itemType.typeName,
                    itemSelected = itemSelected,
                    detailItem = item)
            }
        }
    }
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
            thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/c/f0/5df3fc8b3c883.jpg",
            itemType = ItemType.COMIC
        )
    )
}