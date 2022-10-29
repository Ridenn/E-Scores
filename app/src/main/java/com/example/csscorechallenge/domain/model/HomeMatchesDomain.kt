package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeMatchesDomain(
    val id: Int,
    val beginAt: String? = null,
    val name: String? = null,
    val league: LeagueDomain? = null,
    val serie: SerieDomain? = null,
    val opponents: List<OpponentsDomain>? = null
) : Parcelable