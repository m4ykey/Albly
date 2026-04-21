package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val type : String? = null,
    val uri : String? = null,
    val resource_url : String? = null,
    val uri150 : String? = null,
    val width : Int? = null,
    val height : Int? = null
)
