package com.m4ykey.search.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.search.artist.ResultsArtist
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchArtistUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(perPage : Int = 20, page : Int = 1, query : String, type : String) : Flow<PagingData<ResultsArtist>> {
        return repository.searchArtist(
            page = page,
            perPage = perPage,
            query = query
        )
    }
}