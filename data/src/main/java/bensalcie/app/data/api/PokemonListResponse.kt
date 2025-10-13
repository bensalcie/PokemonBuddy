package bensalcie.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonListResponse(val results: List<PokemonEntry>)
data class PokemonEntry(val name: String, val url: String)

data class PokemonDetailsResponse(
    val name: String,
    val sprites: Sprites?,
    val stats: List<StatEntry>?,
    val moves: List<MoveEntry>?,
    val species: SpeciesEntry?,
    val types: List<TypeEntry>?,
    val weight: Int,
    val height: Int

) {
    data class Sprites(val front_default: String?)
    data class StatEntry(val base_stat: Int, val stat: StatName) {
        data class StatName(val name: String)
    }

    data class MoveEntry(val move: MoveName) {
        data class MoveName(val name: String)
    }

    data class SpeciesEntry(val name: String, val url: String)
    data class TypeEntry(val slot: Int, val type: TypeName) {
        // Represents the inner "type" object, which holds the type's name and URL
        data class TypeName(val name: String, val url: String)
    }

}

/**
 * Retrofit service interface for the Pokemon API.
 * This interface defines the HTTP methods to be used to interact with the Pokemon API.
 * [getPokemonList] is used to retrieve a list of Pokemon entries, capped at 100
 */
interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailsResponse
}
