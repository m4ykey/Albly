package com.m4ykey.albly.album.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrlsDto(
    val spotify: String? = null
)