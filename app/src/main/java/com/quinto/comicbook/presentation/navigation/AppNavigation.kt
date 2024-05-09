package com.quinto.comicbook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.presentation.home.HomeView
import com.quinto.comicbook.presentation.item_detail.ItemDetailView
import com.quinto.comicbook.presentation.item_vlist.ItemVListView
import com.quinto.comicbook.presentation.navigation.AppDestinations.DETAIL
import com.quinto.comicbook.presentation.navigation.AppDestinations.HOME
import com.quinto.comicbook.presentation.navigation.AppDestinations.LIST
import com.quinto.comicbook.presentation.navigation.AppKeys.ITEM_ID_KEY
import com.quinto.comicbook.presentation.navigation.AppKeys.ITEM_TYPE_KEY
import kotlinx.coroutines.runBlocking

private object AppDestinations {
    const val HOME = "home"
    const val LIST = "list"
    const val DETAIL = "detail"
}

private object AppKeys {
    const val ITEM_TYPE_KEY = "itemType"
    const val ITEM_ID_KEY = "itemId"
}

@Composable
fun AppNavigation (
    startDestination: String = HOME,
    repository: ComicBookRepository
) {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            HOME
        ) {
            HomeView(
                itemTypeSelected = actions.itemTypeSelected,
                itemSelected = actions.itemSelected
            )
        }

        composable(
            "$LIST/{$ITEM_TYPE_KEY}",
            arguments = listOf(
                navArgument(ITEM_TYPE_KEY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val itemType = arguments.getString(ITEM_TYPE_KEY)
            ItemVListView(itemType!!, actions.itemSelected, actions.backClicked)
        }

        composable(
            "$DETAIL/{$ITEM_ID_KEY}",
            arguments = listOf(
                navArgument(ITEM_ID_KEY) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val itemId = arguments.getInt(ITEM_ID_KEY)
            val item = runBlocking {
                repository.retrieveItem(itemId)
            }
            ItemDetailView(item, actions.itemSelected, actions.backClicked)
        }
    }
}


private class AppActions(
    navController: NavHostController,
) {
    val itemTypeSelected: (String) -> Unit = { itemType: String ->
        navController.navigate("$LIST/$itemType")
    }
    val itemSelected: (Item) -> Unit = { item: Item ->
        navController.navigate("$DETAIL/${item.id}")
    }
    val backClicked: () -> Unit = {
        navController.popBackStack()
    }
}
