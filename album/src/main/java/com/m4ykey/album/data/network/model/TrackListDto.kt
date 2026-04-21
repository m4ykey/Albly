package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackListDto(
    val position : String? = null,
    val type_ : String? = null,
    val title : String? = null,
    val duration : String? = null
)
