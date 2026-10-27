package com.m4ykey.album.data.network.dto.new_release

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewReleaseResultDto(
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("master_id") val masterId: Int? = null,
    val thumb: String? = null,
    val title: String? = null
)