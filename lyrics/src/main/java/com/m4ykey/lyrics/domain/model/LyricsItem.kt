package com.m4ykey.lyrics.domain.model

data class LyricsItem(
    val albumName : String,
    val artistName : String,
    val id : Int,
    val name : String,
    val plainLyrics : String,
    val syncedLyrics : String,
    val trackName : String
)