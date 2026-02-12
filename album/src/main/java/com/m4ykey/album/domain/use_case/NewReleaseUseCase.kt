package com.m4ykey.album.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class NewReleaseUseCase(
    private val repository: AlbumRepository
) {
    operator fun invoke(limit : Int = 20, offset : Int = 0) : Flow<PagingData<AlbumItem>> {
        return repository.getNewRelease(offset = offset, limit = limit)
    }
}