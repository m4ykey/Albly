package com.m4ykey.artist.data.service

import com.m4ykey.artist.data.dto.ArtistDto
import com.m4ykey.artist.data.dto.ArtistReleaseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ArtistService(private val httpClient : HttpClient) : RemoteArtistService {

    override suspend fun getArtistById(id: Int): ArtistDto {
        return httpClient.get("/artists/$id").body()
    }

    override suspend fun getArtistReleases(
        id: Int,
        page : Int,
        perPage : Int
    ): ArtistReleaseDto {
        return httpClient.get("/artists/$id/releases") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }
}