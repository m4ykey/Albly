package com.m4ykey.lyrics.data.service

import com.m4ykey.lyrics.data.dto.LyricsDtoItem

interface RemoteLyricsService {

    suspend fun searchLyrics(
        q : String,
        trackName : String
    ) : List<LyricsDtoItem>

    suspend fun getLyrics(
        id : Int
    ) : LyricsDtoItem

}