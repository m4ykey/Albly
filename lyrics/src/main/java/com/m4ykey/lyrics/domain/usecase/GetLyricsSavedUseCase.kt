package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.Sort
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.flow.Flow

class GetLyricsSavedUseCase(
    private val repository: LyricsRepository
) {
    operator fun invoke(query : String, sort : Sort) : Flow<List<LyricsEntity>> {
        return when (sort) {
            Sort.LATEST -> {
                repository.getSavedLyricsLatest(query)
            }
            Sort.OLDEST -> {
                repository.getSavedLyricsOldest(query)
            }
            Sort.ALPHABETICAL -> {
                repository.getSavedLyricsAlphabetical(query)
            }
        }
    }
}