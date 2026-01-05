package com.m4ykey.search.data.network.service

import com.m4ykey.core.model.dto.AlbumListDto

interface RemoteSearchService {

    suspend fun search(
        q : String,
        offset : Int,
        limit : Int,
        type : String
    ) : AlbumListDto

}