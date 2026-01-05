package com.m4ykey.album.domain.use_case

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.repository.AlbumRepository

class GetListenLaterAlbumsUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke() : List<AlbumEntity> {
        return repository.getListenLaterAlbums()
    }
}