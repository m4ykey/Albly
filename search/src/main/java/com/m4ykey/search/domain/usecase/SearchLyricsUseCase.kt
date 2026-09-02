package com.m4ykey.search.domain.usecase

import com.m4ykey.search.domain.model.lyrics.GeniusRoot
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchLyricsUseCase (
    private val repository: SearchRepository
) {
    operator fun invoke(query : String) : Flow<List<GeniusRoot>> {
        return repository.searchLyrics(query)
    }
}