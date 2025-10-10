package bensalcie.app.pokmonbuddy

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import bensalcie.app.pokmonbuddy.util.NetworkMonitor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMonitorTest {

    @Test
    fun `returns true when network has internet capability`() {
        val cm = mockk<ConnectivityManager>()
        val nc = mockk<NetworkCapabilities>()

        every { cm.activeNetwork } returns mockk()
        every { cm.getNetworkCapabilities(any()) } returns nc
        every { nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = NetworkMonitor.isNetworkAvailable(cm)
        assertEquals(true, result)
    }

    @Test
    fun `returns false when no active network`() {
        val cm = mockk<ConnectivityManager>()
        every { cm.activeNetwork } returns null

        val result = NetworkMonitor.isNetworkAvailable(cm)
        assertEquals(false, result)
    }
}
