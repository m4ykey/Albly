package com.m4ykey.lyrics.presentation

sealed interface LyricsAction {
    data object OnToggleSave : LyricsAction
    data class OnLoadLyrics(
        val artist : String,
        val track : String,
        val album: String?,
        val image : String? = null
    ) : LyricsAction
}