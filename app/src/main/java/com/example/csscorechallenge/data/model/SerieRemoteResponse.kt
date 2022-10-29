package com.example.csscorechallenge.data.model

import android.os.Parcelable
import com.example.csscorechallenge.domain.model.SerieDomain
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerieRemoteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("full_name")
    val fullName: String? = null
) : Parcelable

fun SerieRemoteResponse.toDomain(): SerieDomain =
    SerieDomain(
        id = id,
        fullName = fullName
    )