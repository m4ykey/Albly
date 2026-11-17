package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.domain.repository.LyricsRepository

class CacheLyricsUseCase(private val repository: LyricsRepository) {
    suspend operator fun invoke(lyrics: LyricsEntity) {
        repository.cacheLyrics(lyrics)
    }
}