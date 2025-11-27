package com.m4ykey.albly.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CopyrightDto(
    val text: String? = null,
    val type: String? = null
)