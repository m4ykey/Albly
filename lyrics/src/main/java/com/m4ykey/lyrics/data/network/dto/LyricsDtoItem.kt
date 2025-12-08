package com.m4ykey.lyrics.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LyricsDtoItem(
    val albumName: String? = null,
    val artistName: String? = null,
    val duration: Double? = null,
    val id: Int? = null,
    val instrumental: Boolean? = false,
    val name: String? = null,
    val plainLyrics: String? = null,
    val syncedLyrics: String? = null,
    val trackName: String? = null
)