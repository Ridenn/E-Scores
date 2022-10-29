package com.example.csscorechallenge.domain.repository

import com.example.csscorechallenge.domain.model.HomeMatchesDomain

interface HomeMatchesRepository {
    suspend fun getHomeMatches(currentPage: Int, limit: Int): List<HomeMatchesDomain>
}