package com.m4ykey.search.presentation

import com.m4ykey.search.domain.model.lyrics.GeniusRoot

sealed interface LyricsSearchState {
    data object Idle : LyricsSearchState
    data object Loading : LyricsSearchState
    data class Error(val message : String) : LyricsSearchState
    data class Success(val songs : List<GeniusRoot>) : LyricsSearchState
}