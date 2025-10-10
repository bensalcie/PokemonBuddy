package bensalcie.app.pokmonbuddy.di

import bensalcie.app.pokmonbuddy.util.NetworkMonitor
import io.mockk.mockk
import org.koin.dsl.module

val testAppModule = module {
    // Provide mocked dependencies
    single { mockk<NetworkMonitor>(relaxed = true) }
}
