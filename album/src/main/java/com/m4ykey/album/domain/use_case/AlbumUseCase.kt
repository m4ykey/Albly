package com.m4ykey.album.domain.use_case

import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class AlbumUseCase (
    private val repository : AlbumRepository
) {
    suspend operator fun invoke(id : String) : Flow<AlbumDetail> {
        return repository.getAlbumById(id)
    }
}