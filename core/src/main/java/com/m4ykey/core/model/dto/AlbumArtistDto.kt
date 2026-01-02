package com.m4ykey.core.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumArtistDto(
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val id: String? = null,
    val name: String? = null,
    val type: String? = null
)