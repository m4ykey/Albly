package com.m4ykey.artist.domain.use_case

import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.Sort
import com.m4ykey.core.database.model.artist.ArtistListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetSavedArtistUseCase(private val repository : ArtistRepository) {
    operator fun invoke(query : String, sort : Sort) : Flow<List<ArtistListItem>> {
        return when (sort) {
            Sort.LATEST -> {
                repository.getSavedArtistLatest(query)
            }
            Sort.OLDEST -> {
                repository.getSavedArtistOldest(query)
            }
            Sort.ALPHABETICAL -> {
                repository.getSavedArtistAlphabetical(query)
            }
        }
    }
}