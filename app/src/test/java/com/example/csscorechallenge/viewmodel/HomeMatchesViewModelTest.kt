package com.example.csscorechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import com.example.csscorechallenge.ui.homematches.viewmodel.HomeMatchesViewModel
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
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeMatchesViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getHomeMatchesUseCase: GetHomeMatchesUseCase

    private lateinit var viewModel: HomeMatchesViewModel

    @Before
    fun setupDispatcher() {
        stopKoin()
        MockitoAnnotations.openMocks(this)
        viewModel = HomeMatchesViewModel(
            getHomeMatchesUseCase = getHomeMatchesUseCase
        )
    }

    @Test
    fun `should get home matches data then returns loading state equal true`() =
        runTest {
            val page = Random.nextInt()
            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            Assert.assertEquals(true, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get keep section data then returns loading state equal false`() =
        runTest {

            val id = Random.nextInt()
            val page = Random.nextInt()
            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = id)
            )

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get home matches data then returns bind data with valid list`() =
        runTest {

            val id = Random.nextInt()
            val page = Random.nextInt()
            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = id)
            )

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.BindData(mockedResponse, swipeToRefresh),
                viewModel.getHomeMatchesLiveData.value
            )
        }

    @Test
    fun `should get home matches data then returns append data with valid list`() =
        runTest {

            val page = Random.nextInt() + 1
            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = Random.nextInt())
            )

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = true,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.AppendData(mockedResponse, swipeToRefresh),
                viewModel.getHomeMatchesLiveData.value
            )
        }

    @Test
    fun `should get home matches data then returns failure error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = Exception()

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.Failure(mockedThrowable),
                viewModel.getHomeMatchesLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get home matches data then returns network error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = UnknownHostException()

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.NetworkError,
                viewModel.getHomeMatchesLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get home matches data then returns timeout error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = SocketTimeoutException()

            whenever(getHomeMatchesUseCase.getHomeMatches(any())).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getHomeMatches(
                page = page,
                appendData = false,
                swipeToRefresh = homeMatchesState.isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.TimeoutError,
                viewModel.getHomeMatchesLiveData.value
            )
            Assert.assertEquals(false, viewModel.showLoadingLiveData.value)
        }

    @Test
    fun `should get running matches data then returns bind data with valid list`() =
        runTest {

            val id = Random.nextInt()
            val page = Random.nextInt()
            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = id)
            )

            whenever(getHomeMatchesUseCase.getRunningHomeMatches()).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getRunningHomeMatches(
                page = page,
                appendData = false,
                isSwipeToRefresh = isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetRunningHomeMatchesState.BindData(
                    mockedResponse,
                    page,
                    false,
                    isSwipeToRefresh
                ),
                viewModel.getRunningHomeMatchesLiveData.value
            )
        }

    @Test
    fun `should get running matches data then returns failure error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = Exception()

            whenever(getHomeMatchesUseCase.getRunningHomeMatches()).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getRunningHomeMatches(
                page = page,
                appendData = false,
                isSwipeToRefresh = isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.Failure(mockedThrowable),
                viewModel.getHomeMatchesLiveData.value
            )
        }

    @Test
    fun `should get running matches data then returns network error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = UnknownHostException()

            whenever(getHomeMatchesUseCase.getRunningHomeMatches()).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getRunningHomeMatches(
                page = page,
                appendData = false,
                isSwipeToRefresh = isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.NetworkError,
                viewModel.getHomeMatchesLiveData.value
            )
        }

    @Test
    fun `should get running matches data then returns timeout error data state`() =
        runTest {

            val page = Random.nextInt()
            val mockedThrowable = SocketTimeoutException()

            whenever(getHomeMatchesUseCase.getRunningHomeMatches()).thenReturn(
                flowOf(Result.failure(mockedThrowable))
            )

            viewModel.getRunningHomeMatches(
                page = page,
                appendData = false,
                isSwipeToRefresh = isSwipeToRefresh
            )
            advanceUntilIdle()

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.TimeoutError,
                viewModel.getHomeMatchesLiveData.value
            )
        }

}