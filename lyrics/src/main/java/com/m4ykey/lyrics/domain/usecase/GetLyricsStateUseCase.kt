package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.database.model.lyrics.LyricsWithState
import com.m4ykey.lyrics.domain.repository.LyricsRepository

class GetLyricsStateUseCase(private val repository : LyricsRepository) {
    suspend operator fun invoke(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist : String? = null
    ) : LyricsWithState? {
        return repository.getLyricsWithState(source, remoteId, isrc, track, artist)
    }
}