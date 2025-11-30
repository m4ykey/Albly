package com.m4ykey.albly.album.data.network.service

import com.m4ykey.albly.album.data.network.dto.AlbumDetailDto
import com.m4ykey.albly.album.data.network.dto.AlbumListDto
import com.m4ykey.albly.album.data.network.dto.AlbumTracksDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AlbumService(
    private val httpClient : HttpClient
) : RemoteAlbumService {

    override suspend fun getNewRelease(
        limit: Int,
        offset: Int
    ): AlbumListDto {
        return httpClient.get("browse/new-releases") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    override suspend fun getAlbumById(id: String): AlbumDetailDto {
        return httpClient.get("albums/$id").body()
    }

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