package com.m4ykey.album.domain.model.detail

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val uri : String,
    val title : String,
    val description : String,
    val duration : Int,
    val embed : Boolean
)
