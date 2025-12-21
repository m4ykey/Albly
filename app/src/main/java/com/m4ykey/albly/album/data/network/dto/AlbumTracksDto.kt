package com.m4ykey.albly.album.data.network.dto

import com.m4ykey.albly.track.data.network.dto.TrackItemDto
import kotlinx.serialization.Serializable

@Serializable
data class AlbumTracksDto(
    val items: List<TrackItemDto>
)