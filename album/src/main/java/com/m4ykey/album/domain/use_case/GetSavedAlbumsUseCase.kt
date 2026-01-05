package com.m4ykey.album.domain.use_case

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.model.type.AlbumType
import kotlinx.coroutines.flow.Flow

class GetSavedAlbumsUseCase(private val repository: AlbumRepository) {
    operator fun invoke(query : String, type : AlbumType?) : Flow<List<AlbumEntity>> {
        return repository.getSavedAlbums(query, type)
    }
}