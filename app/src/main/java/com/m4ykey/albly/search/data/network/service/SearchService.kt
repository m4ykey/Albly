package com.m4ykey.albly.search.data.network.service

import com.m4ykey.albly.album.data.network.model.AlbumListDto
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