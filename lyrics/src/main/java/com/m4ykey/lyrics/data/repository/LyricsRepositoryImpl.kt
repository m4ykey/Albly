package com.m4ykey.lyrics.data.repository

import com.m4ykey.core.database.dao.LyricsDao
import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.core.database.model.lyrics.LyricsWithState
import com.m4ykey.core.network.safeApi
import com.m4ykey.lyrics.data.mapper.toDomain
import com.m4ykey.lyrics.data.service.RemoteLyricsService
import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRedItem
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LyricsRepositoryImpl(
    private val service : RemoteLyricsService,
    private val dao : LyricsDao
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
        trackName: String,
        artistName : String,
        albumName : String?
    ): Flow<List<LyricsItem>> = flow {
        val result = safeApi {
            service.searchLyrics(
                trackName = trackName,
                artistName = artistName,
                albumName = albumName
            ).map { it.toDomain() }
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

    override suspend fun getRedLyrics(isrc: String): Flow<String> = flow {
        val result = safeApi {
            service.getRedLyrics(isrc)
        }

        result.fold(
            onSuccess = { emit(it) },
            onFailure = { throw it }
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun searchRedLyrics(q: String): Flow<List<LyricsRedItem>> = flow {
        val result = safeApi {
            service.searchRedLyrics(q).hits.map { it.toDomain() }
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

    override suspend fun insertLyrics(lyrics: LyricsEntity) {
        dao.insertLyrics(lyrics)
    }

    override suspend fun insertSavedLyrics(isLyricsSaved: IsLyricsSaved) {
        dao.insertSavedLyrics(isLyricsSaved)
    }

    override fun getLyricsById(id: Int): LyricsEntity? {
        return dao.getLyricsById(id)
    }

    override fun getSavedLyricsState(trackKey: String): IsLyricsSaved? {
        return dao.getSavedLyricsState(trackKey)
    }

    override suspend fun getLyricsWithState(id: Int): LyricsWithState? {
        return dao.getLyricsWithState(id)
    }

    override suspend fun deleteLyrics(id: Int) {
        return dao.deleteLyrics(id)
    }

    override suspend fun deleteSavedLyricsState(trackKey: String) {
        return dao.deleteSavedLyricsState(trackKey)
    }

    override fun getSavedLyricsLatest(query: String): Flow<List<LyricsEntity>> {
        return dao.getSavedLyricsLatest(query)
    }

    override fun getSavedLyricsOldest(query: String): Flow<List<LyricsEntity>> {
        return dao.getSavedLyricsOldest(query)
    }

    override fun getSavedLyricsAlphabetical(query: String): Flow<List<LyricsEntity>> {
        return dao.getSavedLyricsAlphabetical(query)
    }
}