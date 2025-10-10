package bensalcie.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonListResponse(val results: List<PokemonEntry>)
data class PokemonEntry(val name: String, val url: String)

data class PokemonDetailsResponse(
    val name: String,
    val sprites: Sprites?,
    val stats: List<StatEntry>?
) {
    data class Sprites(val front_default: String?)
    data class StatEntry(val base_stat: Int, val stat: StatName) {
        data class StatName(val name: String)
    }
}

/**
 * Retrofit service interface for the Pokemon API.
 * This interface defines the HTTP methods to be used to interact with the Pokemon API.
 * [getPokemonList] is used to retrieve a list of Pokemon entries, capped at 100
 */
interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int = 100): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailsResponse
}
