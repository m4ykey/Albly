package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.dto.new_release.NewReleaseRootDto
import java.time.LocalDate

interface RemoteNewReleaseAlbumService {

    suspend fun getNewReleases(
        perPage : Int = 20,
        page : Int = 1,
        year : Int = LocalDate.now().year,
        releaseDate : String
    ) : NewReleaseRootDto

}