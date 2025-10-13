package bensalcie.app.data.utils

import bensalcie.app.core.util.Constants.IMAGE_BASE_URL
import bensalcie.app.data.api.PokemonDetailsResponse
import bensalcie.app.data.api.PokemonEntry
import bensalcie.app.data.api.PokemonListResponse
import bensalcie.app.domain.model.Move
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.domain.model.Stat
import bensalcie.app.domain.model.Type

object PokemonMappers {

    fun mapToDomain(entry: PokemonEntry): Pokemon {
        val id = entry.url.trimEnd('/').split("/").last()
        val imageUrl =
            "${IMAGE_BASE_URL}$id.png"
        return Pokemon(entry.name, imageUrl)
    }

    fun mapDetailsToDomain(details: PokemonDetailsResponse): PokemonDetails {
        val stats = details.stats?.map { Stat(it.stat.name, it.base_stat) } ?: emptyList()
        val moves = details.moves?.map { Move(it.move.name) } ?: emptyList()
        val types = details.types?.map { Type(it.type.name) } ?: emptyList()

        return PokemonDetails(
            name = details.name,
            imageUrl = details.sprites?.front_default,
            stats = stats,
            moves = moves,
            speciesName = details.species?.name ?: "",
            types = types,
            weight = details.weight,
            height = details.height
        )
    }
}
