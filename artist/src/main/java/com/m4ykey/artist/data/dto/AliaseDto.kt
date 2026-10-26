package com.m4ykey.artist.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AliaseDto(
    val id: Int? = null,
    val name: String? = null,
    val resource_url: String? = null
)