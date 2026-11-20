package com.m4ykey.lyrics.presentation

import com.m4ykey.lyrics.domain.model.LyricsItem

sealed interface LyricsUiState {
    data object Loading : LyricsUiState
    data class Error(
        val message : String,
        val isRefreshing: Boolean = false
    ) : LyricsUiState
    data class Success(
        val item : LyricsItem,
        val isSaved : Boolean = false,
        val isRefreshing : Boolean = false
    ) : LyricsUiState
}