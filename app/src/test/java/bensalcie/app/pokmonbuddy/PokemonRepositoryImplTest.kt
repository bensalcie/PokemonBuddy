package bensalcie.app.pokmonbuddy

import bensalcie.app.core.network.ApiResult
import bensalcie.app.data.api.PokeApiService
import bensalcie.app.data.repository.PokemonRepositoryImpl

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {

    private val api = mockk<PokeApiService>()
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setup() {
        repository = PokemonRepositoryImpl(api)
    }

    @Test
    fun `returns Error when API throws exception`() = runTest {
        coEvery { api.getPokemonList() } throws RuntimeException("Network error")

        val result = repository.getPokemonList()

        assertTrue(result is ApiResult.Error)
        assertEquals("Network error", (result as ApiResult.Error).throwable.message)
    }
}
