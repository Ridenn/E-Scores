package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LeagueDomain(
    val id: Int,
    val imageUrl: String? = null,
    val name: String? = null
) : Parcelable