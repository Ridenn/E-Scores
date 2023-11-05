package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.ResultsDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultsRemoteResponse(

    @SerializedName("score")
    val score: Int? = null,

    @SerializedName("team_id")
    val teamId: Int
) : Parcelable

fun ResultsRemoteResponse.toDomain(): ResultsDomain =
    ResultsDomain(
        score = score,
        teamId = teamId
    )