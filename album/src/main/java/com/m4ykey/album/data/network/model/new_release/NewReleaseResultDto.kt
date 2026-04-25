package com.m4ykey.album.data.network.model.new_release

import kotlinx.serialization.Serializable

@Serializable
data class NewReleaseResultDto(
    val cover_image: String? = null,
    val master_id: Int? = null,
    val thumb: String? = null,
    val title: String? = null
)