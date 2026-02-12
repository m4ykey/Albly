package com.m4ykey.album.data.network.service

interface RemoteAlbumService {

    suspend fun getNewRelease(
        limit : Int,
        offset : Int
    ) : AlbumListDto

    suspend fun getAlbumById(
        id : String
    ) : AlbumDetailDto

}