package com.m4ykey.album.domain.usecase

import com.m4ykey.album.data.local.model.AlbumListItem
import com.m4ykey.album.data.local.model.AlbumSort
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