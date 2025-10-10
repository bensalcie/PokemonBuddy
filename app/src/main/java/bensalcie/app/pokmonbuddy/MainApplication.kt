package bensalcie.app.pokmonbuddy

import android.app.Application
import bensalcie.app.core.di.NetworkModule
import bensalcie.app.data.api.PokeApiService
import bensalcie.app.data.repository.PokemonRepositoryImpl
import bensalcie.app.domain.repository.PokemonRepository
import bensalcie.app.domain.usecase.GetPokemonDetailsUseCase
import bensalcie.app.domain.usecase.GetPokemonListUseCase
import bensalcie.app.pokmonbuddy.details.DetailsViewModel
import bensalcie.app.pokmonbuddy.home.HomeViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import retrofit2.Retrofit

import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single<Retrofit> { NetworkModule.createRetrofit("https://pokeapi.co/api/v2/") }
            single { get<Retrofit>().create(PokeApiService::class.java) }
            single<PokemonRepository> { PokemonRepositoryImpl(get()) }
            single { GetPokemonListUseCase(get()) }
            single { GetPokemonDetailsUseCase(get()) }

            viewModel { HomeViewModel(get()) }
            viewModel { (name: String) -> DetailsViewModel(get(), name) }
        }

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
