package com.m4ykey.lyrics.data.service

import com.m4ykey.lyrics.data.dto.LyricsDtoItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LyricsService(
    private val httpClient: HttpClient
) : RemoteLyricsService {

    override suspend fun searchLyrics(
        trackName: String,
        artistName : String,
        albumName : String?
    ): List<LyricsDtoItem> {
        return httpClient.get("search") {
            parameter("track_name", trackName)
            parameter("artist_name", artistName)
            parameter("album_name", albumName)
        }.body()
    }

    override suspend fun getLyrics(id: Int): LyricsDtoItem {
        return httpClient.get("get/$id").body()
    }
}