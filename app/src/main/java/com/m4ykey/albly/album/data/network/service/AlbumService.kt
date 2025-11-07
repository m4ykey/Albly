package com.m4ykey.albly.album.data.network.service

import com.m4ykey.albly.album.data.network.model.AlbumDetailDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AlbumService {

    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : AlbumDetailDto

}