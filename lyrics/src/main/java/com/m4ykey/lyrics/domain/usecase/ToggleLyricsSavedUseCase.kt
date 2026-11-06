package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToggleLyricsSavedUseCase(private val repository: LyricsRepository) {
    suspend operator fun invoke(
        lyrics : LyricsEntity,
        isCurrentlySaved : Boolean
    ) = withContext(Dispatchers.IO) {
        if (isCurrentlySaved) {
            repository.deleteSavedLyricsState(
                lyrics.source,
                lyrics.remoteId,
                lyrics.isrc
            )
            repository.deleteLyrics(
                lyrics.source,
                lyrics.remoteId,
                lyrics.isrc
            )
        } else {
            repository.insertLyrics(lyrics)
            repository.insertSavedLyrics(
                IsLyricsSaved(
                    isLyricsSaved = true,
                    isrc = lyrics.isrc,
                    source = lyrics.source,
                    remoteId = lyrics.remoteId
                )
            )
        }
    }
}