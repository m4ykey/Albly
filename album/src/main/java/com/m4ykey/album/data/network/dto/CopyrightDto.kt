package com.m4ykey.album.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CopyrightDto(
    val text: String? = null,
    val type: String? = null
)