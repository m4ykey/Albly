package com.m4ykey.core.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistListDto(
    val artists: ArtistsDto
)