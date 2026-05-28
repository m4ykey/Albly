package com.m4ykey.search.data.network.model.dto.artist

import kotlinx.serialization.Serializable

@Serializable
data class ResultsArtistDto(
    val id : Int? = null,
    val title : String? = null,
    val cover_image : String? = null,
    val thumb : String? = null
)
