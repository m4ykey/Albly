package com.m4ykey.core.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistItemDto(
    val external_urls: ExternalUrlsDto? = null,
    val followers: FollowersDto? = null,
    val id: String? = null,
    val images: List<ImageDto>? = null,
    val name: String? = null,
    val popularity: Int? = null
)