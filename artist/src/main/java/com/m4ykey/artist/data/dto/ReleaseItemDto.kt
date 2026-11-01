package com.m4ykey.artist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseItemDto(
    val artist: String? = null,
    val format: String? = null,
    val id: Int? = null,
    val label: String? = null,
    @SerialName("main_release") val mainRelease: Int? = null,
    @SerialName("resource_url") val resourceUrl: String? = null,
    val role: String? = null,
    val status: String? = null,
    val thumb: String? = null,
    val title: String? = null,
    @SerialName("trackinfo") val trackInfo: String? = null,
    val type: String? = null,
    val year: Int? = null
)