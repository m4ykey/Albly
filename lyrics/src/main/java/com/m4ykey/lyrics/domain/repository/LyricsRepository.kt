package com.m4ykey.lyrics.domain.repository

import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.core.database.model.lyrics.LyricsWithState
import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRedItem
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun getLyrics(id : Int) : Flow<LyricsItem>
    suspend fun searchLyrics(trackName : String, artistName : String, albumName : String?) : Flow<List<LyricsItem>>
    suspend fun getRedLyrics(isrc : String) : Flow<String>
    suspend fun searchRedLyrics(q : String) : Flow<List<LyricsRedItem>>

    suspend fun insertLyrics(lyrics : LyricsEntity)
    suspend fun insertSavedLyrics(isLyricsSaved: IsLyricsSaved)
    fun getLyricsById(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist : String? = null
    ) : LyricsEntity?
    fun getSavedLyricsState(source : String, remoteId : Int, isrc : String?) : IsLyricsSaved?
    suspend fun getLyricsWithState(
        source : String? = null,
        remoteId : Int? = null,
        isrc : String? = null,
        track : String? = null,
        artist : String? = null
    ) : LyricsWithState?
    suspend fun deleteLyrics(source : String, remoteId : Int, isrc : String?)
    suspend fun deleteSavedLyricsState(source : String, remoteId : Int, isrc : String?)
    fun getSavedLyricsLatest(query : String) : Flow<List<LyricsEntity>>
    fun getSavedLyricsOldest(query: String) : Flow<List<LyricsEntity>>
    fun getSavedLyricsAlphabetical(query: String) : Flow<List<LyricsEntity>>

}