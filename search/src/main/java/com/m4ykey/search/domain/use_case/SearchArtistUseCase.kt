package com.m4ykey.search.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchArtistUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(limit : Int = 20, offset : Int = 0, q : String, type: String) : Flow<PagingData<ArtistItem>> {
        return repository.searchArtist(
            limit = limit,
            offset = offset,
            q = q,
            type = type
        )
    }
}