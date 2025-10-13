package bensalcie.app.pokmonbuddy.navigation

sealed class NavRoutes(val route: String) {

    data object Home : NavRoutes(NavigationConstants.ROUTE_HOME)

    data object Details :
        NavRoutes("${NavigationConstants.ROUTE_DETAILS}/{${NavigationConstants.ARG_POKEMON_NAME}}") {

        fun createRoute(name: String): String =
            "${NavigationConstants.ROUTE_DETAILS}/$name"
    }
}
