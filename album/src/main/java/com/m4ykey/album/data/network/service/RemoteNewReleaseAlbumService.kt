package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.model.new_release.NewReleaseRootDto
import java.time.LocalDate

interface RemoteNewReleaseAlbumService {

    suspend fun getNewReleases(
        perPage : Int = 20,
        page : Int,
        type : String = "master",
        format : String = "album",
        year : Int = LocalDate.now().year,
        releaseDate : String
    ) : NewReleaseRootDto

}