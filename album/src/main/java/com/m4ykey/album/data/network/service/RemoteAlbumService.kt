package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.model.detail.AlbumRootDto

interface RemoteAlbumService {

    suspend fun getAlbum(
        id : Int
    ) : AlbumRootDto

}