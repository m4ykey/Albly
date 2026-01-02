package com.m4ykey.track.data.network.service

import com.m4ykey.track.data.network.dto.AlbumTracksDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TrackService(
    private val httpClient : HttpClient
) : RemoteTrackService {

    override suspend fun getAlbumTracks(
        id: String,
        limit: Int,
        offset: Int
    ): AlbumTracksDto {
        return httpClient.get("albums/$id/tracks") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

}