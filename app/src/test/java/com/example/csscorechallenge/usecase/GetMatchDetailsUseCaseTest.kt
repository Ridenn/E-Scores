package com.example.csscorechallenge.usecase

import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.repository.MatchDetailsRepository
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
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
class GetMatchDetailsUseCaseTest {

    private lateinit var useCase: GetMatchDetailsUseCase

    @Mock
    lateinit var repository: MatchDetailsRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupDispatcher() {
        MockitoAnnotations.openMocks(this)
        useCase = GetMatchDetailsUseCase(repository, Dispatchers.Main)
    }

    @Test
    fun `should get match details repository then returns failure`() =
        runTest {
            val page = Random.nextInt()

            whenever(repository.getMatchDetails(any())).thenAnswer { throw Throwable() }

            useCase.getMatchDetails(page).collect { result ->
                Assert.assertEquals(true, result.isFailure)
            }
        }

    @Test
    fun `should get match details repository then returns success`() =
        runTest {
            val id = Random.nextInt()

            val mockedResponse = MatchDetailsDomain(id = id)

            whenever(repository.getMatchDetails(any())).thenReturn(mockedResponse)

            useCase.getMatchDetails(id).collect { result ->
                Assert.assertEquals(true, result.isSuccess)
                Assert.assertEquals(mockedResponse, result.getOrNull())
            }
        }
}