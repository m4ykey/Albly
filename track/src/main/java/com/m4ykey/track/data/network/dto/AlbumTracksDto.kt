package com.m4ykey.track.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumTracksDto(
    val items: List<TrackItemDto>
)