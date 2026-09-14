package com.m4ykey.lyrics.data.service

import com.m4ykey.lyrics.data.dto.LyricsDtoItem

interface RemoteLyricsService {

    suspend fun searchLyrics(
        trackName : String,
        artistName : String,
        albumName : String?
    ) : List<LyricsDtoItem>

    suspend fun getLyrics(
        id : Int
    ) : LyricsDtoItem

}