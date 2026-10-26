package com.m4ykey.lyrics.domain.model

data class LyricsRedItem(
    val album : String,
    val artist : String,
    val color : String,
    val cover : String,
    val duration : Double,
    val isrc : String,
    val title : String,
    val year : Int
)
