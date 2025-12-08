package com.m4ykey.lyrics.domain.repository

import com.m4ykey.lyrics.data.model.LyricsItem
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun getLyrics(artistName : String, trackName : String) : Flow<LyricsItem>

}