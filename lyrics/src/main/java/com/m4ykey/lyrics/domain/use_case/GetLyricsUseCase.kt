@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.lyrics.domain.use_case

import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetLyricsUseCase(private val repository: LyricsRepository) {
    suspend operator fun invoke(artist : String, track : String) : Flow<LyricsItem> {
        return repository.searchLyrics(q = artist, trackName = track)
            .flatMapLatest { results ->
                val firstId = results.firstOrNull()?.id
                if (firstId != null) {
                    repository.getLyrics(id = firstId)
                } else {
                    flow { throw Exception("Lyrics not found for this track") }
                }
            }
    }
}