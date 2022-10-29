package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpponentsDomain(
    val opponent: OpponentDomain? = null,
    val type: String? = null
) : Parcelable