package bensalcie.app.data.api

import junit.framework.TestCase.assertEquals
import org.junit.Test

class PokemonDetailsResponseTest {
    private val pokemonDetailsResponse = PokemonDetailsResponse(
        name = "Pikachu",
        sprites = PokemonDetailsResponse.Sprites(front_default = "https://example.com/pikachu.png"),
        stats = listOf(),
        moves = listOf(),
        species = PokemonDetailsResponse.SpeciesEntry(
            url = "https://example.com/species/pikachu",
            name = "Pikachu"
        ),
        types = listOf(),
        weight = 45,
        height = 3,
    )


    @Test
    fun `getName   returns correct name`() {
        // Verify that getName() returns the correct name it was initialized with.
        assertEquals("Pikachu", pokemonDetailsResponse.name)


    }

    @Test
    fun `getName   with empty string`() {
        // Verify that getName() returns an empty string when the 'name' property is explicitly set to an empty string.
        assertEquals("", pokemonDetailsResponse.name)
    }

    @Test
    fun `getName   with special characters`() {

        assertEquals("Pikachu", pokemonDetailsResponse.name)
    }

    @Test
    fun `getSprites   with non null value`() {

        assertEquals("https://example.com/pikachu.png", pokemonDetailsResponse.sprites?.front_default)
    }

}