package com.example.csscorechallenge.repository

import com.example.csscorechallenge.MainCoroutineRule
import com.example.csscorechallenge.data.model.MatchDetailsRemoteResponse
import com.example.csscorechallenge.data.model.toDomain
import com.example.csscorechallenge.data.repository.matchdetails.MatchDetailsRepositoryImpl
import com.example.csscorechallenge.data.service.AppService
import com.example.csscorechallenge.domain.repository.MatchDetailsRepository
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
class MatchDetailsRepositoryTest {

    private val service = mock<AppService>()

    private lateinit var repository: MatchDetailsRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = MatchDetailsRepositoryImpl(service)
    }

    @Test
    fun `should get home matches data then returns data and call service method`() =
        runTest {
            val id = Random.nextInt()
            val mockedResponse =
                MatchDetailsRemoteResponse(
                    id = id,
                    name = null,
                    players = null
                )

            whenever(service.getMatchDetails(any())).thenReturn(mockedResponse)

            assertEquals(
                repository.getMatchDetails(any()),
                mockedResponse.toDomain()
            )

            verify(service, times(1)).getMatchDetails(any())
        }
}