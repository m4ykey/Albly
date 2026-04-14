package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistsDto(
    val name : String,
    val thumbnail_url : String
)
