package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumEntity

class GetLocalAlbumUseCase(private val repository: AlbumRepository) {
    operator fun invoke(id : Int) : AlbumEntity? {
        return repository.getLocalAlbumById(id)
    }
}