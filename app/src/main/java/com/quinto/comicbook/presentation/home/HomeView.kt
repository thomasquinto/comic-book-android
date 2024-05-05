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
                    ItemHListView(ItemType.STORY.typeName, itemTypeSelected, itemSelected)
                }
            }
        }
    )
}
