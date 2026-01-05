package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.dto.AlbumDetailDto
import com.m4ykey.core.model.dto.AlbumListDto

interface RemoteAlbumService {

    suspend fun getNewRelease(
        limit : Int,
        offset : Int
    ) : AlbumListDto

    suspend fun getAlbumById(
        id : String
    ) : AlbumDetailDto

}