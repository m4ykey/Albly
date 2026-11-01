package com.m4ykey.album.data.network.dto.detail

import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val uri : String? = null,
    val title : String? = null,
    val description : String? = null,
    val duration : Int? = null,
    val embed : Boolean = false
)
