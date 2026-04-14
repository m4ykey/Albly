package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val type : String,
    val uri : String,
    val resource_url : String,
    val uri150 : String,
    val width : Int,
    val height : Int
)
