package com.example.csscorechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
import com.example.csscorechallenge.ui.homematches.viewmodel.HomeMatchesViewModel
import com.example.csscorechallenge.ui.matchdetails.viewmodel.MatchDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MatchDetailsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getMatchDetailsUseCase: GetMatchDetailsUseCase

    private lateinit var viewModel: MatchDetailsViewModel

    @Before
    fun setupDispatcher() {
        stopKoin()
        MockitoAnnotations.openMocks(this)
        viewModel = MatchDetailsViewModel(
            getMatchDetailsUseCase = getMatchDetailsUseCase
        )
    }

    @Test
    fun `should get team details data then returns loading state equal true`() =
        runTest {
            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            Assert.assertEquals(true, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get team details data then returns loading state equal false`() =
        runTest {

            val mockedResponse = MatchDetailsDomain(id = Random.nextInt())

            whenever(getMatchDetailsUseCase.getMatchDetails(any())).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            advanceUntilIdle()

            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get team details data then returns bind data with valid list`() =
        runTest {

            val mockedResponse = MatchDetailsDomain(id = Random.nextInt())

            whenever(getMatchDetailsUseCase.getMatchDetails(any())).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            advanceUntilIdle()

            Assert.assertEquals(
                MatchDetailsViewModel.GetMatchDetailsState.BindData(
                    mockedResponse,
                    isFirstTeam = true,
                    match = null
                ),
                viewModel.getMatchDetailsLiveData.value
            )
        }

    @Test
    fun `should get team details data then returns failure error data state`() =
        runTest {

            val mockedThrowable = Exception()

            whenever(getMatchDetailsUseCase.getMatchDetails(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            advanceUntilIdle()

            Assert.assertEquals(
                MatchDetailsViewModel.GetMatchDetailsState.Failure(mockedThrowable),
                viewModel.getMatchDetailsLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get team details data then returns network error data state`() =
        runTest {

            val mockedThrowable = UnknownHostException()

            whenever(getMatchDetailsUseCase.getMatchDetails(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            advanceUntilIdle()

            Assert.assertEquals(
                MatchDetailsViewModel.GetMatchDetailsState.NetworkError,
                viewModel.getMatchDetailsLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get team details data then returns timeout error data state`() =
        runTest {

            val mockedThrowable = SocketTimeoutException()

            whenever(getMatchDetailsUseCase.getMatchDetails(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getTeamDetails(id = 1, isFirstTeam = true, match = null)
            advanceUntilIdle()

            Assert.assertEquals(
                MatchDetailsViewModel.GetMatchDetailsState.TimeoutError,
                viewModel.getMatchDetailsLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }
}