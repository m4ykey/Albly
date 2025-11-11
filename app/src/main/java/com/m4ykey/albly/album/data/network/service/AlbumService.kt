package com.m4ykey.albly.album.data.network.service

import com.m4ykey.albly.album.data.network.model.AlbumDetailDto
import com.m4ykey.albly.album.data.network.model.AlbumTracksDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumService {

    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : AlbumDetailDto

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : AlbumTracksDto

}