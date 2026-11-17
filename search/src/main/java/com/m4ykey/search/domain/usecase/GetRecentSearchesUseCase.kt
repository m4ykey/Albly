package com.m4ykey.search.domain.usecase

import com.m4ykey.core.database.model.search.SearchItem
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class GetRecentSearchesUseCase(private val repository: SearchRepository) {
    operator fun invoke() : Flow<List<SearchItem>> {
        return repository.getSearchItemList()
    }
}