package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackListDto(
    val position : String,
    val type_ : String,
    val title : String,
    val duration : String
)
