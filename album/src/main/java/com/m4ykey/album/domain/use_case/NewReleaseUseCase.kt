package com.m4ykey.album.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class NewReleaseUseCase(
    private val repository: AlbumRepository
) {
    operator fun invoke() : Flow<PagingData<NewReleaseResult>> {
        return repository.getNewRelease()
    }
}