package com.m4ykey.lyrics.data.network.service

import com.m4ykey.lyrics.data.network.dto.LyricsDtoItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LyricsService(
    private val httpClient: HttpClient
) : RemoteLyricsService {

    override suspend fun searchLyrics(
        q: String,
        trackName: String
    ): List<LyricsDtoItem> {
        return httpClient.get("api/search") {
            parameter("q", q)
            parameter("track_name", trackName)
        }.body()
    }

    override suspend fun getLyrics(id: Int): LyricsDtoItem {
        return httpClient.get("api/get/$id").body()
    }
}