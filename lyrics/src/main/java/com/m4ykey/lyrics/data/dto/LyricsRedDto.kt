package com.m4ykey.lyrics.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LyricsRedDto(
    val hits: List<LyricsRedItemDto> = emptyList(),
    val next: String? = null
)