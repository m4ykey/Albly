package com.m4ykey.lyrics.data.repository

import com.m4ykey.core.network.safeApi
import com.m4ykey.lyrics.data.mapper.toDomain
import com.m4ykey.lyrics.data.model.LyricsItem
import com.m4ykey.lyrics.data.network.service.RemoteLyricsService
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LyricsRepositoryImpl(
    private val service : RemoteLyricsService
) : LyricsRepository {

    override suspend fun getLyrics(
        artistName: String,
        trackName: String
    ): Flow<LyricsItem> {
        return flow {
            val result = safeApi {
                val response = service.getLyrics(
                    artistName = artistName,
                    trackName = trackName
                )
                val lyrics = response.map { it.toDomain() }
                lyrics.firstOrNull() ?: throw Exception("Lyrics not found")
            }

            val lyrics = result.fold(
                onSuccess = { it },
                onFailure = { throw Exception("Lyrics not found") }
            )
            emit(lyrics)
        }
    }
}