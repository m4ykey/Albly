package com.m4ykey.search.data.network.service

import com.m4ykey.core.model.dto.AlbumListDto
import com.m4ykey.core.model.dto.ArtistListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchService(
    private val httpClient: HttpClient
) : RemoteSearchService {

    override suspend fun searchAlbum(
        q: String,
        offset: Int,
        limit: Int,
        type: String,
        market : String
    ): AlbumListDto {
        return httpClient.get("search") {
            parameter("q", q)
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("type", type)
            parameter("market", market)
        }.body()
    }

    override suspend fun searchArtist(
        q: String,
        type: String,
        limit: Int,
        offset: Int,
        market : String
    ): ArtistListDto {
        return httpClient.get("search") {
            parameter("q", q)
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("type", type)
            parameter("market", market)
        }.body()
    }
}