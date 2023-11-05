package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultsDomain(
    val score: Int? = null,
    val teamId: Int
) : Parcelable