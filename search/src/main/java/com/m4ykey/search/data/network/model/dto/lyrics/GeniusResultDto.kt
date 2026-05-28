package com.m4ykey.search.data.network.model.dto.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class GeniusResultDto(
    val title : String? = null,
    val artist_names : String? = null,
    val song_art_image_url : String? = null
)
