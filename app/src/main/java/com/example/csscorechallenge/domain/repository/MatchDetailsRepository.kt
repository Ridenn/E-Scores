package com.example.csscorechallenge.domain.repository

import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain

interface MatchDetailsRepository {
    suspend fun getMatchDetails(id: Int): MatchDetailsDomain
}