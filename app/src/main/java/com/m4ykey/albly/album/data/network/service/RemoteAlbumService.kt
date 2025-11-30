package com.m4ykey.albly.album.data.network.service

import com.m4ykey.albly.album.data.network.dto.AlbumDetailDto
import com.m4ykey.albly.album.data.network.dto.AlbumListDto
import com.m4ykey.albly.album.data.network.dto.AlbumTracksDto

interface RemoteAlbumService {

    suspend fun getNewRelease(
        limit : Int,
        offset : Int
    ) : AlbumListDto

    suspend fun getAlbumById(
        id : String
    ) : AlbumDetailDto

    suspend fun getAlbumTracks(
        id : String,
        limit : Int,
        offset : Int
    ) : AlbumTracksDto

}