package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumWithStates

class GetAlbumStateUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(id : Int) : AlbumWithStates? {
        return repository.getAlbumWithStates(id = id)
    }
}