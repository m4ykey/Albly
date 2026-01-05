package com.m4ykey.album.domain.use_case

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class GetRandomAlbumUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke() : Flow<AlbumEntity> {
        return repository.getRandomAlbum()
    }
}