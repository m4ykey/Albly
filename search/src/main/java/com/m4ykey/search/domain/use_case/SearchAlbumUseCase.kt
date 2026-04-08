package com.m4ykey.search.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.search.ResultsAlbum
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchAlbumUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(perPage : Int = 20, page : Int = 1, query : String, type : String) : Flow<PagingData<ResultsAlbum>> {
        return repository.searchAlbum(
            page = page,
            perPage = perPage,
            query = query
        )
    }
}