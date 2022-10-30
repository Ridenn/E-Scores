package com.example.csscorechallenge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerDomain(
    val firstName: String? = null,
    val nickName: String? = null,
    val imageUrl: String? = null
) : Parcelable