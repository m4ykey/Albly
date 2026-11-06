package com.m4ykey.search.domain.usecase

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.lyrics.Hit
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchLyricsUseCase (
    private val repository: SearchRepository
) {
    operator fun invoke(query : String) : Flow<PagingData<Hit>> {
        return repository.searchLyrics(query)
    }
}