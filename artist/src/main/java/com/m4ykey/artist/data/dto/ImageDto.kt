package com.m4ykey.artist.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val height: Int? = null,
    val resource_url: String? = null,
    val type: String? = null,
    val uri: String? = null,
    val uri150: String? = null,
    val width: Int? = null
)