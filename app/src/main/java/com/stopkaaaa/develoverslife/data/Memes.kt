package com.stopkaaaa.develoverslife.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Memes (
    @SerialName("result")
    val list: List<Mem>
)