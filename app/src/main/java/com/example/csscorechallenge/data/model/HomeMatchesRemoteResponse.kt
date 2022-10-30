package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeMatchesRemoteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("begin_at")
    val beginAt: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("league")
    val league: LeagueRemoteResponse?,

    @SerializedName("serie")
    val serie: SerieRemoteResponse?,

    @SerializedName("opponents")
    val opponents: List<OpponentsRemoteResponse>?

) : Parcelable

fun HomeMatchesRemoteResponse.toDomain(): HomeMatchesDomain =
    HomeMatchesDomain(
        id = id,
        beginAt = beginAt,
        status = status,
        name = name,
        league = league?.toDomain(),
        serie = serie?.toDomain(),
        opponents = opponents?.map { it.toDomain() }
    )