package bensalcie.app.pokmonbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bensalcie.app.pokmonbuddy.home.HomeScreen
import bensalcie.app.pokmonbuddy.details.DetailsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onPokemonClick = { name ->
                navController.navigate("details/$name")
            })
        }
        composable(
            "details/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            DetailsScreen(name, onBack = { navController.popBackStack() })
        }
    }
}
