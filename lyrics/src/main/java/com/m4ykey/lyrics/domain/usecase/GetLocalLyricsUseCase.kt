package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.lyrics.domain.repository.LyricsRepository

class GetLocalLyricsUseCase(private val repository: LyricsRepository) {
    operator fun invoke(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist : String? = null
    ) : LyricsEntity? {
        return repository.getLyricsById(source, remoteId, isrc, track, artist)
    }
}