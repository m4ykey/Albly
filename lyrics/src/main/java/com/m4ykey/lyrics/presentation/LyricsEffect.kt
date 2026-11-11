package com.m4ykey.lyrics.presentation

sealed interface LyricsEffect {
    data class ShowError(val message : String) : LyricsEffect
}