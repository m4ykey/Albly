package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumEntity

class CacheAlbumUseCase(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(album : AlbumEntity) {
        repository.cacheAlbum(album)
    }
}