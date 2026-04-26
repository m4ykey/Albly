package com.m4ykey.lyrics.presentation

import com.m4ykey.lyrics.domain.model.LyricsItem

sealed interface LyricsUiState {
    data object Loading : LyricsUiState
    data class Error(val message : String) : LyricsUiState
    data class Success(val item : LyricsItem) : LyricsUiState
}