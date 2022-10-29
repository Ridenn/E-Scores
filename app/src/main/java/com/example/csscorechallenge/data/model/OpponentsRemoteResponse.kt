package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.OpponentsDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpponentsRemoteResponse(

    @SerializedName("opponent")
    val opponent: OpponentRemoteResponse? = null,

    @SerializedName("type")
    val type: String? = null
) : Parcelable

fun OpponentsRemoteResponse.toDomain(): OpponentsDomain =
    OpponentsDomain(
        opponent = opponent?.toDomain(),
        type = type
    )