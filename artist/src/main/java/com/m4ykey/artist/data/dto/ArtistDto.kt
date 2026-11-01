package com.m4ykey.artist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val aliases: List<AliaseDto> = emptyList(),
    @SerialName("data_quality") val dataQuality: String? = null,
    val id: Int? = null,
    val images: List<ImageDto> = emptyList(),
    val name: String? = null,
    @SerialName("namevariations") val nameVariations: List<String> = emptyList(),
    val profile: String? = null,
    @SerialName("realname") val realName: String? = null,
    @SerialName("releases_url") val releasesUrl: String? = null,
    @SerialName("resource_url") val resourceUrl: String? = null,
    val uri: String? = null,
    val urls: List<String> = emptyList()
)