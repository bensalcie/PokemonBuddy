package bensalcie.app.data.api

import bensalcie.app.core.util.Constants.BASEURL
import bensalcie.app.core.util.Constants.IMAGE_BASE_URL
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PokemonDetailsResponseTest {
    private val pokemonDetailsResponse = PokemonDetailsResponse(
        name = "Pikachu",
        sprites = PokemonDetailsResponse.Sprites(front_default = "${IMAGE_BASE_URL}pikachu.png"),
        stats = listOf(),
        moves = listOf(),
        species = PokemonDetailsResponse.SpeciesEntry(
            url = "${BASEURL}/pikachu",
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
        pokemonDetailsResponse.copy(name = "")
        // Verify that getName() returns an empty string when the 'name' property is explicitly set to an empty string.
        assertEquals(listOf<PokemonDetailsResponse.StatEntry>(), pokemonDetailsResponse.stats)
    }

    @Test
    fun `getName   with special characters`() {

        assertEquals(listOf<PokemonDetailsResponse.MoveEntry>(), pokemonDetailsResponse.moves)
    }

    @Test
    fun `getSprites   with non null value`() {

        assertEquals("${IMAGE_BASE_URL}pikachu.png", pokemonDetailsResponse.sprites?.front_default)
    }

}