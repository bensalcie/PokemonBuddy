package bensalcie.app.navigation

import bensalcie.app.pokmonbuddy.navigation.NavRoutes
import bensalcie.app.pokmonbuddy.navigation.NavigationConstants
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationConstantsTest {

    @Test
    fun `details route composes correctly`() {
        val name = "pikachu"
        val route = NavRoutes.Details.createRoute(name)
        assertEquals("${NavigationConstants.ROUTE_DETAILS}/$name", route)
    }
}
