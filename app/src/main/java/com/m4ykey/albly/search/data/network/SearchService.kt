package com.m4ykey.albly.search.data.network

import com.m4ykey.albly.album.data.network.AlbumListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token : String,
        @Query("type") type : String,
        @Query("q") q : String,
        @Query("offset") offset : Int,
        @Query("limit") limit : Int
    ) : AlbumListDto

}