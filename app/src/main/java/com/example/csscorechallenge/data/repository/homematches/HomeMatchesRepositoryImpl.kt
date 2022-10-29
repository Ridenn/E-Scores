package com.example.csscorechallenge.data.repository.homematches

import com.example.csscorechallenge.data.model.toDomain
import com.example.csscorechallenge.data.service.AppService
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import javax.inject.Inject

class HomeMatchesRepositoryImpl @Inject constructor(
    private val service: AppService
) : HomeMatchesRepository {

    override suspend fun getHomeMatches(
        currentPage: Int,
        limit: Int
    ): List<HomeMatchesDomain> {
        val result = service.getHomeMatches(currentPage, limit)
        return result.map {
            it.toDomain()
        }
    }
}