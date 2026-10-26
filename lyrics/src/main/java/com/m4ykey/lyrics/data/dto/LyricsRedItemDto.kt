package com.m4ykey.lyrics.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LyricsRedItemDto(
    val album: String? = null,
    val artist: String? = null,
    val color: String? = null,
    val cover: String? = null,
    val duration: Double? = 0.0,
    val isrc: String? = null,
    val title: String? = null,
    val year: Int? = 0
)