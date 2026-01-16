package com.m4ykey.core.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistsDto(
    val items: List<ArtistItemDto>? = emptyList()
)