package com.m4ykey.core.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrlsDto(
    val spotify: String? = null
)