package com.m4ykey.lyrics.data.network.service

import com.m4ykey.lyrics.data.network.dto.LyricsDtoItem

interface RemoteLyricsService {

    suspend fun searchLyrics(
        q : String,
        trackName : String
    ) : List<LyricsDtoItem>

    suspend fun getLyrics(
        id : Int
    ) : LyricsDtoItem

}