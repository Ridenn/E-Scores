package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.LeagueDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LeagueRemoteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("name")
    val name: String? = null
) : Parcelable

fun LeagueRemoteResponse.toDomain(): LeagueDomain =
    LeagueDomain(
        id = id,
        imageUrl = imageUrl,
        name = name
    )