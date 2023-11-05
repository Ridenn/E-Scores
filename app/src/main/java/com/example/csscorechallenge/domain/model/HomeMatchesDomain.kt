package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import com.example.csscorechallenge.data.model.OpponentsRemoteResponse
import com.example.csscorechallenge.data.model.ResultsRemoteResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeMatchesDomain(
    val id: Int,
    val beginAt: String? = null,
    val status: String? = null,
    val name: String? = null,
    val league: LeagueDomain? = null,
    val serie: SerieDomain? = null,
    val results: List<ResultsDomain>? = null,
    val opponents: List<OpponentsDomain>? = null,
    val numberOfGames: Int? = null,
) : Parcelable