package com.stopkaaaa.develoverslife.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mem(
    @SerialName("id")
    val id: Int,

    @SerialName("description")
    val description: String,

    @SerialName("gifURL")
    val gifUrl: String
)
