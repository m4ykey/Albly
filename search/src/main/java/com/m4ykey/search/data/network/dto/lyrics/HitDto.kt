package com.m4ykey.search.data.network.dto.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class HitDto(
    val album: String? = null,
    val artist: String? = null,
    val color: String? = null,
    val cover: String? = null,
    val duration: Double? = 0.0,
    val isrc: String? = null,
    val title: String? = null,
    val year: Int? = null
)