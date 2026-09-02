package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.dto.detail.AlbumRootDto

interface RemoteAlbumService {

    suspend fun getAlbum(
        id : Int
    ) : AlbumRootDto

}