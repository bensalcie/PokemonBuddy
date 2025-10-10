package bensalcie.app.domain.model

data class Stat(val name: String, val value: Int)
data class PokemonDetails(val name: String, val imageUrl: String?, val stats: List<Stat>)
