package com.example.csscorechallenge.repository

import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.data.model.HomeMatchesRemoteResponse
import com.example.csscorechallenge.data.model.toDomain
import com.example.csscorechallenge.data.repository.homematches.HomeMatchesRepositoryImpl
import com.example.csscorechallenge.data.service.AppService
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.*
import kotlin.random.Random

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeMatchesRepositoryTest {

    private val service = mock<AppService>()

    private lateinit var repository: HomeMatchesRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = HomeMatchesRepositoryImpl(service)
    }

    @Test
    fun `should get home matches data then returns data and call service method`() =
        runTest {
            val id = Random.nextInt()
            val mockedResponse = mutableListOf(
                HomeMatchesRemoteResponse(
                    id = id,
                    beginAt = null,
                    status = null,
                    name = null,
                    league = null,
                    serie = null,
                    opponents = null
                )
            )

            whenever(service.getHomeMatches(any(), any())).thenReturn(mockedResponse)

            assertEquals(
                repository.getHomeMatches(any(), any()),
                mockedResponse.map { it.toDomain() }
            )

            verify(service, times(1)).getHomeMatches(any(), any())
        }

    @Test
    fun `should get running home matches data then returns data and call service method`() =
        runTest {
            val id = Random.nextInt()
            val mockedResponse = mutableListOf(
                HomeMatchesRemoteResponse(
                    id = id,
                    beginAt = null,
                    status = null,
                    name = null,
                    league = null,
                    serie = null,
                    opponents = null
                )
            )

            whenever(service.getRunningHomeMatches()).thenReturn(mockedResponse)

            assertEquals(
                repository.getRunningHomeMatches(),
                mockedResponse.map { it.toDomain() }
            )

            verify(service, times(1)).getRunningHomeMatches()
        }
}