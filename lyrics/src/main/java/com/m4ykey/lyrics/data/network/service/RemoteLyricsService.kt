package com.m4ykey.lyrics.data.network.service

import com.m4ykey.lyrics.data.network.dto.LyricsDtoItem

interface RemoteLyricsService {

    suspend fun getLyrics(
        artistName : String,
        trackName : String
    ) : List<LyricsDtoItem>

}