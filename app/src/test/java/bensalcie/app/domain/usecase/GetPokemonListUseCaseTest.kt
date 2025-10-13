package bensalcie.app.domain.usecase

import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


class GetPokemonListUseCaseTest {

    // 1. Declare and initialize the mock repository and the use case instance directly.
    // This prevents the UninitializedPropertyAccessException.
    private val mockRepository: PokemonRepository = mockk()
    private val useCase: GetPokemonListUseCase = GetPokemonListUseCase(mockRepository)

    // The @BeforeEach setup function is no longer needed.

    @Test
    fun `invoke with default parameters should call repository with 0 and 100`() = runTest {
        // GIVEN
        val expectedList = listOf(
            Pokemon("Pikachu", "url/pika"),
            Pokemon("Bulbasaur", "url/bulb")
        )
        val successResult = ApiResult.Success(expectedList)

        // Setup the mock to return a flow of the expected result
        // We expect the default parameters (0, 100) to be used here
        coEvery { mockRepository.getPokemonList(0, 100) } returns flowOf(successResult)

        // WHEN
        val resultFlow = useCase() // Call without arguments uses defaults

        // Collect the first item from the flow to trigger the call
        val actualResult = resultFlow.first()

        // THEN
        // 1. Verify the repository function was called exactly once with the default arguments
        verify(exactly = 1) { mockRepository.getPokemonList(0, 100) }

        // 2. Verify that the emitted result is the expected success data
        assertTrue(actualResult is ApiResult.Success)
        assertEquals(expectedList, (actualResult as ApiResult.Success).data)
    }

    @Test
    fun `invoke with custom parameters should call repository with custom offset and limit`() = runTest {
        // GIVEN
        val customOffset = 20
        val customLimit = 5
        val expectedList = listOf(Pokemon("Squirtle", "url/squirt"))
        val successResult = ApiResult.Success(expectedList)

        // Setup the mock to return a flow of the expected result
        // We expect the custom parameters (20, 5) to be used here
        coEvery { mockRepository.getPokemonList(customOffset, customLimit) } returns flowOf(successResult)

        // WHEN
        val resultFlow = useCase(offset = customOffset, limit = customLimit)

        // Collect the first item from the flow to trigger the call
        val actualResult = resultFlow.first()

        // THEN
        // 1. Verify the repository function was called exactly once with the custom arguments
        verify(exactly = 1) { mockRepository.getPokemonList(customOffset, customLimit) }

        // 2. Verify that the emitted result is the expected success data
        assertTrue(actualResult is ApiResult.Success)
        assertEquals(expectedList, (actualResult as ApiResult.Success).data)
    }
}

