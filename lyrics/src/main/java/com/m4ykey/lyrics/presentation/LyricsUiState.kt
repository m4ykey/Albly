package com.m4ykey.lyrics.presentation

import com.m4ykey.lyrics.domain.model.LyricsItem

data class LyricsUiState(
    val loading : Boolean = false,
    val error : String? = null,
    val data : LyricsItem? = null
)