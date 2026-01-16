package com.m4ykey.lyrics.data.repository

import com.m4ykey.core.network.safeApi
import com.m4ykey.lyrics.data.mapper.toDomain
import com.m4ykey.lyrics.data.network.service.RemoteLyricsService
import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LyricsRepositoryImpl(
    private val service : RemoteLyricsService
) : LyricsRepository {

    override suspend fun getLyrics(id: Int): Flow<LyricsItem> = flow {
        val result = safeApi {
            service.getLyrics(id).toDomain()
        }

        result.fold(
            onSuccess = { lyricsItem ->
                emit(lyricsItem)
            },
            onFailure = { error ->
                throw error
            }
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun searchLyrics(
        q: String,
        trackName: String
    ): Flow<List<LyricsItem>> = flow {
        val result = safeApi {
            service.searchLyrics(q = q, trackName = trackName).map { it.toDomain() }
        }

        result.fold(
            onSuccess = { lyricsItem ->
                emit(lyricsItem)
            },
            onFailure = { error ->
                throw error
            }
        )
    }
}