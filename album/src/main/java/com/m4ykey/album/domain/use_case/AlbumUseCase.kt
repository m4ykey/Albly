package com.m4ykey.album.domain.use_case

import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class AlbumUseCase (
    private val repository : AlbumRepository
) {
}