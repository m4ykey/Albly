package com.m4ykey.album.data.network.service

import com.m4ykey.album.data.network.dto.new_release.NewReleaseRootDto
import java.time.LocalDate

interface RemoteNewReleaseAlbumService {

    suspend fun getNewReleases(
        perPage : Int,
        page : Int,
        year : Int
    ) : NewReleaseRootDto

}