package com.m4ykey.albly.track.data.network.service

import com.m4ykey.albly.album.data.network.dto.AlbumTracksDto

interface RemoteTrackService {

    suspend fun getAlbumTracks(
        id : String,
        limit : Int,
        offset : Int
    ) : AlbumTracksDto

}