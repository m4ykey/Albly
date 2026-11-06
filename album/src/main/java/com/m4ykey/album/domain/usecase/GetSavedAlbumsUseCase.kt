package com.m4ykey.album.domain.usecase

import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.core.Sort
import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class GetSavedAlbumsUseCase(
    private val repository: AlbumRepository
) {
    operator fun invoke(query : String, sort : Sort) : Flow<List<AlbumListItem>> {
        return when (sort) {
            Sort.LATEST -> {
                repository.getSavedAlbumsLatest(query)
            }
            Sort.OLDEST -> {
                repository.getSavedAlbumsOldest(query)
            }
            Sort.ALPHABETICAL -> {
                repository.getSavedAlbumsAlphabetical(query)
            }
        }
    }
}