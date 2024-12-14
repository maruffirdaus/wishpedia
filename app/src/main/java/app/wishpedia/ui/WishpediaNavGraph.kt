package app.wishpedia.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.wishpedia.ui.categorizeditems.CategorizedItemsScreen
import app.wishpedia.ui.items.ItemsScreen
import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    object ItemsScreen

    @Serializable
    data class CategorizedItemsScreen(
        val categoryId: Int
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishpediaNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ItemsScreen
    ) {
        composable<Screen.ItemsScreen> {
            ItemsScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategorizedItemsScreen(categoryId))
                }
            )
        }
        composable<Screen.CategorizedItemsScreen> { entry ->
            val args = entry.toRoute<Screen.CategorizedItemsScreen>()
            CategorizedItemsScreen(
                categoryId = args.categoryId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}