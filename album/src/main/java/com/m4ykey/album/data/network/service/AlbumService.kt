package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.model.AlbumRootDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AlbumService(
    private val httpClient : HttpClient
) : RemoteAlbumService {

    override suspend fun getAlbum(id: Int): AlbumRootDto {
        return httpClient.get("masters/$id").body()
    }

}