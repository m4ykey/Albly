package com.m4ykey.lyrics.domain.repository

import com.m4ykey.lyrics.domain.model.LyricsItem
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun getLyrics(id : Int) : Flow<LyricsItem>
    suspend fun searchLyrics(trackName : String, artistName : String, albumName : String?) : Flow<List<LyricsItem>>

}