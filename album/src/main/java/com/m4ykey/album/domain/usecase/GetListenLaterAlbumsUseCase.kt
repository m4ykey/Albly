package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumListItem

class GetListenLaterAlbumsUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke() : List<AlbumListItem> {
        return repository.getListenLaterAlbums()
    }
}