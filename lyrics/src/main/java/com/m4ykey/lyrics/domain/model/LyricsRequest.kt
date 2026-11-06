package com.m4ykey.lyrics.domain.model

data class LyricsRequest(
    val artist : String,
    val track : String,
    val album : String?,
    val image : String?
)
