@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetLyricsUseCase(private val repository: LyricsRepository) {
    suspend operator fun invoke(artist : String, track : String, album: String?) : Flow<LyricsItem> {
        return repository.searchLyrics(trackName = track, artistName = artist, albumName = album)
            .flatMapLatest { results ->
                val matchingItem = results.firstOrNull { item ->
                    item.trackName.equals(track, ignoreCase = true) &&
                    item.artistName.equals(artist, ignoreCase = true) &&
                            (album == null || item.albumName.equals(album, ignoreCase = true))
                }

                if (matchingItem != null) {
                    repository.getLyrics(id = matchingItem.id)
                } else {
                    flow { throw Exception("Lyrics not found for this track") }
                }
            }
    }
}