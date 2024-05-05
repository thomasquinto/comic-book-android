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
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.presentation.item_hlist.ItemHListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    itemTypeSelected: ((String) -> Unit)? = null,
    itemSelected: ((Item) -> Unit)? = null
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

                /*
                item {
                    ItemHListView(ItemType.CHARACTER.typeName, itemTypeSelected, itemSelected,
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
                 */

                item {
                    ItemHListView(ItemType.COMIC.typeName, itemTypeSelected, itemSelected)
                }
                item {
                    ItemHListView(ItemType.CHARACTER.typeName, itemTypeSelected, itemSelected)
                }
                item {
                    ItemHListView(ItemType.SERIES.typeName, itemTypeSelected, itemSelected)
                }
                item {
                    ItemHListView(ItemType.CREATOR.typeName, itemTypeSelected, itemSelected)
                }
                item {
                    ItemHListView(ItemType.EVENT.typeName, itemTypeSelected, itemSelected)
                }
                item {
                    ItemHListView(ItemType.STORY.typeName, itemTypeSelected)
                }
            }
        }
    )

    /*
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
     */

}
