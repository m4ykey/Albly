package com.m4ykey.albly.album.presentation.detail

import com.m4ykey.albly.album.domain.model.AlbumDetail

data class DetailUiState(
    val loading : Boolean = false,
    val error : String? = null,
    val item : AlbumDetail? = null,
    val isSaved : Boolean = false,
    val isListenLaterSaved : Boolean = false
)
