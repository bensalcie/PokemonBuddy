package bensalcie.app.pokmonbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bensalcie.app.pokmonbuddy.details.DetailsScreen
import bensalcie.app.pokmonbuddy.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationConstants.ROUTE_HOME
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(onPokemonClick = { name ->
                navController.navigate(NavRoutes.Details.createRoute(name))
            })
        }

        composable(NavRoutes.Details.route) { backStackEntry ->
            val name =
                backStackEntry.arguments?.getString(NavigationConstants.ARG_POKEMON_NAME).orEmpty()
            DetailsScreen(name = name, onBack = { navController.popBackStack() })
        }
    }
}
