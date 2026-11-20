package com.m4ykey.search.domain.usecase

import com.m4ykey.core.database.model.search.SearchItem
import com.m4ykey.search.domain.repository.SearchRepository

class DeleteSearchItemUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(item : SearchItem) {
        repository.deleteSearchItem(item)
    }

    suspend operator fun invoke() {
        repository.deleteAllSearchItems()
    }
}