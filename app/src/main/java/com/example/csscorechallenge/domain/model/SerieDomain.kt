package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerieDomain(
    val id: Int,
    val fullName: String? = null
) : Parcelable