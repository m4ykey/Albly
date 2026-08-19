package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.model.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.model.dto.artist.SearchArtistRootDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchService(
    private val httpClient: HttpClient
) : RemoteSearchService {

    override suspend fun searchAlbum(
        query : String,
        perPage : Int,
        page : Int
    ): SearchAlbumRootDto {
        return httpClient.get("search") {
            parameter("q", query)
            parameter("format", "album")
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("type", "master")
        }.body()
    }

    override suspend fun searchArtist(
        query: String,
        perPage: Int,
        page: Int
    ): SearchArtistRootDto {
        return httpClient.get("search") {
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("type", "artist")
        }.body()
    }
}