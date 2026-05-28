package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.model.dto.lyrics.GeniusRootDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchLyricsService(
    private val httpClient : HttpClient
) : RemoteSearchLyricsService {

    override suspend fun searchLyrics(query: String): GeniusRootDto {
        return httpClient.get("search") {
            parameter("q", query)
        }.body()
    }
}