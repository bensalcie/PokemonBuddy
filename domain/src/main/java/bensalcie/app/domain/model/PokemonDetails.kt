package bensalcie.app.domain.model

data class Stat(val name: String, val value: Int)
data class Move(val name: String)
data class Type(val name: String)

data class PokemonDetails(
    val name: String,
    val imageUrl: String?,
    val stats: List<Stat>,
    val moves: List<Move>,
    val speciesName: String,
    val types: List<Type>,
    val weight: Int,
    val height: Int
)
