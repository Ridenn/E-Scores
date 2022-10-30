package com.example.csscorechallenge.data.repository.matchdetails

import com.example.csscorechallenge.data.model.toDomain
import com.example.csscorechallenge.data.service.AppService
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import com.example.csscorechallenge.domain.repository.MatchDetailsRepository
import javax.inject.Inject

class MatchDetailsRepositoryImpl @Inject constructor(
    private val service: AppService
) : MatchDetailsRepository {

    override suspend fun getMatchDetails(id: Int): MatchDetailsDomain {
        val result = service.getMatchDetails(id)
        return result.toDomain()
    }
}