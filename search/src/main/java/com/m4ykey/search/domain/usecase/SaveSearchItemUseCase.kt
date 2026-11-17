package com.m4ykey.search.domain.usecase

import com.m4ykey.search.domain.repository.SearchRepository

class SaveSearchItemUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(query : String) {
        repository.saveSearchItem(query)
    }
}