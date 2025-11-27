package com.m4ykey.albly.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumTracksDto(
    val items: List<TrackItemDto>
)