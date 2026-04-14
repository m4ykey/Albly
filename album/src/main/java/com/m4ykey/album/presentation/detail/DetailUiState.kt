package com.m4ykey.album.presentation.detail

import com.m4ykey.album.domain.model.AlbumRoot

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Error(val message : String) : DetailUiState

    data class Success(
        val item: AlbumRoot,
        val isSaved: Boolean = false,
        val isListenLaterSaved: Boolean = false
    ) : DetailUiState
}
