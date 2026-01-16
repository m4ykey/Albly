package com.m4ykey.search.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchAlbumUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(limit : Int = 20, offset : Int = 0, q : String, type : String) : Flow<PagingData<AlbumItem>> {
        return repository.searchAlbum(
            limit = limit,
            offset = offset,
            q = q,
            type = type
        )
    }
}