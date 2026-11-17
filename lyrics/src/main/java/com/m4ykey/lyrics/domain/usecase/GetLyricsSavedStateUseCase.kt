package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.domain.repository.LyricsRepository

class GetLyricsSavedStateUseCase(private val repository: LyricsRepository) {
    suspend operator fun invoke(source : String, remoteId : Int, isrc : String?) : IsLyricsSaved? {
        return repository.getSavedLyricsState(source, remoteId, isrc)
    }
}