package com.m4ykey.album.presentation.detail

import com.m4ykey.album.domain.model.detail.AlbumRoot

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Error(
        val message : String,
        val isRefreshing: Boolean = false
    ) : DetailUiState

    data class Success(
        val item: AlbumRoot,
        val isSaved: Boolean = false,
        val isListenLaterSaved: Boolean = false,
        val isRefreshing : Boolean = false
    ) : DetailUiState
}
