package com.m4ykey.search.data.network.service

import com.m4ykey.core.model.dto.AlbumListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchService(
    private val httpClient: HttpClient
) : RemoteSearchService {

    override suspend fun search(
        q: String,
        offset: Int,
        limit: Int,
        type: String
    ): AlbumListDto {
        return httpClient.get("search") {
            parameter("q", q)
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("type", type)
        }.body()
    }
}