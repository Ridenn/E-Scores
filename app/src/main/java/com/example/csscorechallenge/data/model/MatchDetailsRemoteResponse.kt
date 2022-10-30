package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchDetailsRemoteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String?,

    @SerializedName("players")
    val players: List<PlayerRemoteResponse>?

) : Parcelable

fun MatchDetailsRemoteResponse.toDomain(): MatchDetailsDomain =
    MatchDetailsDomain(
        id = id,
        name = name,
        players = players?.map { it.toDomain() }
    )