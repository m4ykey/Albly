package com.m4ykey.search.data.network.dto.lyrics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeniusResultDto(
    val title : String? = null,
    @SerialName("artist_names") val artistNames : String? = null,
    @SerialName("song_art_image_url") val songArtImageUrl : String? = null
)
