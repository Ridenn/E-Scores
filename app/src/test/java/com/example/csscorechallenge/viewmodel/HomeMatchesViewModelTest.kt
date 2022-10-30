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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
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
    fun `should get home matches data then returns bind data with valid list`() =
        runTest {
            val mockedResponse = mutableListOf(
                HomeMatchesDomain(id = Random.nextInt())
            )

            `when`(getHomeMatchesUseCase.getHomeMatches(1)).thenReturn(
                flowOf(Result.success(mockedResponse))
            )

            viewModel.getHomeMatches(page = 1, appendData = false)
            advanceUntilIdle()

//            val value = LiveDataTestUtil.getValue(viewModel.getHomeMatchesLiveData)
            val value = viewModel.getHomeMatchesLiveData.value

            Assert.assertEquals(
                HomeMatchesViewModel.GetHomeMatchesState.BindData(mockedResponse),
                value
            )
        }
}