package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.model.dto.lyrics.GeniusRootDto

interface RemoteSearchLyricsService {

    suspend fun searchLyrics(
        query : String
    ) : GeniusRootDto

}