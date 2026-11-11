package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumEntity
import kotlinx.coroutines.flow.Flow

class GetRandomAlbumUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke() : Flow<AlbumEntity> {
        return repository.getRandomAlbum()
    }
}