package com.m4ykey.albly.search.data.network.api

import com.m4ykey.albly.search.data.network.model.SearchDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token : String,
        @Query("q") query : String,
        @Query("type") type : String = "album,artist",
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : SearchDto

}