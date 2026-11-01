package com.m4ykey.artist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliaseDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("resource_url") val resourceUrl: String? = null
)