package com.m4ykey.albly.album.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val height: Int? = null,
    val url: String? = null,
    val width: Int? = null
)