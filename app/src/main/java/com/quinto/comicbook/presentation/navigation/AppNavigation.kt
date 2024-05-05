package com.quinto.comicbook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quinto.comicbook.presentation.home.HomeView
import com.quinto.comicbook.presentation.item_vlist.ItemVListView
import com.quinto.comicbook.presentation.navigation.AppDestinations.HOME
import com.quinto.comicbook.presentation.navigation.AppDestinations.LIST
import com.quinto.comicbook.presentation.navigation.AppKeys.ITEM_TYPE_KEY

private object AppDestinations {
    const val HOME = "home"
    const val LIST = "list"
    const val DETAIL = "detail"
}

private object AppKeys {
    const val ITEM_TYPE_KEY = "itemType"
}

@Composable
fun AppNavigation(
    startDestination: String = HOME,
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
            HomeView(itemTypeSelected = actions.itemTypeSelected)
        }
        composable(
            "{$LIST}/{$ITEM_TYPE_KEY}",
            arguments = listOf(
                navArgument(ITEM_TYPE_KEY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val itemType = arguments.getString(ITEM_TYPE_KEY)
            ItemVListView(itemType!!)
        }
    }
}

private class AppActions(
    navController: NavHostController
) {
    val itemTypeSelected: (String) -> Unit = { itemType: String ->
        navController.navigate("{$LIST}/$itemType")
    }
}
