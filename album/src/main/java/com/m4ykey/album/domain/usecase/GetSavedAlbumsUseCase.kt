package com.m4ykey.album.domain.usecase

import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.album.domain.model.AlbumSort
import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class GetSavedAlbumsUseCase(
    private val repository: AlbumRepository
) {
    operator fun invoke(query : String, sort : AlbumSort) : Flow<List<AlbumListItem>> {
        return when (sort) {
            AlbumSort.LATEST -> {
                repository.getSavedAlbumsLatest(query)
            }
            AlbumSort.OLDEST -> {
                repository.getSavedAlbumsOldest(query)
            }
            AlbumSort.ALPHABETICAL -> {
                repository.getSavedAlbumsAlphabetical(query)
            }
        }
    }
}