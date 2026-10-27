package com.m4ykey.album.data.network.dto.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackListDto(
    val position : String? = null,
    @SerialName("type_") val type : String? = null,
    val title : String? = null,
    val duration : String? = null
)
