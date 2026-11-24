package com.m4ykey.artist.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistReleaseDto(
    val releases: List<ReleaseItemDto> = emptyList(),
    val pagination : PaginationDto? = null
)