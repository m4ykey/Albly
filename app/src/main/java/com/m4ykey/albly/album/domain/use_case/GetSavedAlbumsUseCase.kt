package com.m4ykey.albly.album.domain.use_case

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import kotlinx.coroutines.flow.Flow

class GetSavedAlbumsUseCase(private val repository: AlbumRepository) {
    operator fun invoke(query : String, type : AlbumType?) : Flow<List<AlbumEntity>> {
        return repository.getSavedAlbums(query, type)
    }
}