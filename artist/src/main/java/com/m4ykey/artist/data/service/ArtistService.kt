package com.m4ykey.artist.data.service

import com.m4ykey.artist.data.dto.ArtistDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArtistService(private val httpClient : HttpClient) : RemoteArtistService {

    override suspend fun getArtistById(id: Int): ArtistDto {
        return httpClient.get("/artists/$id").body()
    }
}