package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.OpponentDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpponentRemoteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null
) : Parcelable

fun OpponentRemoteResponse.toDomain(): OpponentDomain =
    OpponentDomain(
        id = id,
        name = name,
        imageUrl = imageUrl
    )