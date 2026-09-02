package com.m4ykey.album.domain.usecase

import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.domain.repository.AlbumRepository

class GetAlbumStateUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(id : Int) : AlbumWithStates? {
        return repository.getAlbumWithStates(id = id)
    }
}