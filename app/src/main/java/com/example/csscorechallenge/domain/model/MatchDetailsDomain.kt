package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchDetailsDomain(
    val id: Int,
    val name: String? = null,
    val players: List<PlayerDomain>? = null
) : Parcelable