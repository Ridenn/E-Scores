package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpponentDomain(
    val id: Int,
    val name: String? = null,
    val imageUrl: String? = null
) : Parcelable