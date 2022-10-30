package com.example.csscorechallenge.usecase

import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.random.Random

@ExperimentalCoroutinesApi
class GetHomeMatchesUseCaseTest {

    private lateinit var useCase: GetHomeMatchesUseCase

    @Mock
    lateinit var repository: HomeMatchesRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupDispatcher() {
        MockitoAnnotations.openMocks(this)
        useCase = GetHomeMatchesUseCase(repository, Dispatchers.Main)
    }

    @Test
    fun `should get home matches repository then returns failure`() =
        runTest {
            val page = Random.nextInt()

            whenever(repository.getHomeMatches(any(), any())).thenAnswer { throw Throwable() }

            useCase.getHomeMatches(page).collect { result ->
                Assert.assertEquals(true, result.isFailure)
            }
        }

    @Test
    fun `should get home matches repository then returns success`() =
        runTest {
            val id = Random.nextInt()
            val page = Random.nextInt()

            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = id)
            )

            whenever(repository.getHomeMatches(any(), any())).thenReturn(mockedResponse)

            useCase.getHomeMatches(page).collect { result ->
                Assert.assertEquals(true, result.isSuccess)
                Assert.assertEquals(mockedResponse, result.getOrNull())
            }
        }

    @Test
    fun `should get running home matches repository then returns failure`() =
        runTest {
            whenever(repository.getRunningHomeMatches()).thenAnswer { throw Throwable() }

            useCase.getRunningHomeMatches().collect { result ->
                Assert.assertEquals(true, result.isFailure)
            }
        }

    @Test
    fun `should get running home matches repository then returns success`() =
        runTest {
            val id = Random.nextInt()

            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = id)
            )

            whenever(repository.getRunningHomeMatches()).thenReturn(mockedResponse)

            useCase.getRunningHomeMatches().collect { result ->
                Assert.assertEquals(true, result.isSuccess)
                Assert.assertEquals(mockedResponse, result.getOrNull())
            }
        }
}