package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.dto.new_release.NewReleaseRootDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NewReleaseAlbumService(
    private val httpClient : HttpClient
) : RemoteNewReleaseAlbumService {

    override suspend fun getNewReleases(
        perPage: Int,
        page: Int,
        year: Int,
        releaseDate: String
    ): NewReleaseRootDto {
        return httpClient.get("search") {
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("type", "master")
            parameter("format", "album")
            parameter("year", year)
            parameter("release_date", releaseDate)
            parameter("sort", "year")
            parameter("sort_order", "desc")
        }.body()
    }

}