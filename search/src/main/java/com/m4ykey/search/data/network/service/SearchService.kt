package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.dto.artist.SearchArtistRootDto
import com.m4ykey.search.data.network.dto.lyrics.LyricsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchService(
    private val dHttpClient: HttpClient,
    private val lHttpClient: HttpClient
) : RemoteSearchService {

    override suspend fun searchAlbum(
        query : String,
        perPage : Int,
        page : Int
    ): SearchAlbumRootDto {
        return dHttpClient.get("search") {
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
        return dHttpClient.get("search") {
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("type", "artist")
        }.body()
    }

    override suspend fun searchLyrics(
        query: String,
        after : String?
    ): LyricsDto {
        return lHttpClient.get("/search.json") {
            parameter("q", query)
            parameter("after", after)
        }.body()
    }
}