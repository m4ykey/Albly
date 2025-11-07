package com.m4ykey.albly.album.presentation

import com.m4ykey.albly.album.domain.model.AlbumDetail

data class DetailUiState(
    val loading : Boolean = false,
    val error : String? = null,
    val item : AlbumDetail? = null
)
