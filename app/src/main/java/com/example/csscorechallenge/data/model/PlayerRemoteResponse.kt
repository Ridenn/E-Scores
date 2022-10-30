package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.OpponentDomain
import com.example.csscorechallenge.domain.model.PlayerDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerRemoteResponse(

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("name")
    val nickName: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null
) : Parcelable

fun PlayerRemoteResponse.toDomain(): PlayerDomain =
    PlayerDomain(
        firstName = firstName,
        nickName = nickName,
        imageUrl = imageUrl
    )