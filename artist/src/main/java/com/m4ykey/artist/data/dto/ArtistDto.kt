package com.m4ykey.artist.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val aliases: List<AliaseDto> = emptyList(),
    val data_quality: String? = null,
    val id: Int? = null,
    val images: List<ImageDto> = emptyList(),
    val name: String? = null,
    val namevariations: List<String> = emptyList(),
    val profile: String? = null,
    val realname: String? = null,
    val releases_url: String? = null,
    val resource_url: String? = null,
    val uri: String? = null,
    val urls: List<String> = emptyList()
)