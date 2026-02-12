package com.m4ykey.track.data.network.service

interface RemoteTrackService {

    suspend fun getAlbumTracks(
        id : String,
        limit : Int,
        offset : Int
    ) : AlbumTracksDto

}