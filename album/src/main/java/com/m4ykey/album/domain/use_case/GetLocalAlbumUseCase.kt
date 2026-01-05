package com.m4ykey.album.domain.use_case

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.repository.AlbumRepository

class GetLocalAlbumUseCase(private val repository: AlbumRepository) {
    operator fun invoke(id : String) : AlbumEntity? {
        return repository.getLocalAlbumById(id)
    }
}