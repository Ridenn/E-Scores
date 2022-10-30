package com.example.csscorechallenge.data.service

import com.example.csscorechallenge.data.model.HomeMatchesRemoteResponse
import com.example.csscorechallenge.data.model.MatchDetailsRemoteResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {

    @GET("csgo/matches/upcoming")
    suspend fun getHomeMatches(
        @Query(value = "page[number]") page: Int,
        @Query(value = "page[size]") limit: Int
    ) : List<HomeMatchesRemoteResponse>

    @GET("csgo/matches/running")
    suspend fun getRunningHomeMatches(
    ) : List<HomeMatchesRemoteResponse>

    @GET("teams/{id}")
    suspend fun getMatchDetails(
        @Path(value = "id") id: Int,
    ) : MatchDetailsRemoteResponse
}