package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.model.new_release.NewReleaseRootDto

interface RemoteNewReleaseAlbumService {

    suspend fun getNewReleases(
        perPage : Int = 20,
        page : Int,
        type : String = "master",
        format : String = "album",
        year : Int = 2026,
        releaseDate : String
    ) : NewReleaseRootDto

}