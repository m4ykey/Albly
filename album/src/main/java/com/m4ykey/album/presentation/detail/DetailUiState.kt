package com.m4ykey.album.presentation.detail

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Error(val message : String) : DetailUiState

    data class Success(
        val item: AlbumDetail,
        val isSaved: Boolean = false,
        val isListenLaterSaved: Boolean = false
    ) : DetailUiState
}
