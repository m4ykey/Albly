package com.m4ykey.albly.search.data.network.service

import com.m4ykey.albly.album.data.network.dto.AlbumListDto

interface RemoteSearchService {

    suspend fun search(
        q : String,
        offset : Int,
        limit : Int,
        type : String
    ) : AlbumListDto

}